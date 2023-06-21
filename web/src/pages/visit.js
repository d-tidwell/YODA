import yodaClient from '../api/yodaClient';
import BindingClass from "../util/bindingClass";
import Header from '../components/header';
import DataStore from "../util/DataStore";

class Visit extends BindingClass {
    constructor() {
      super();
      this.bindClassMethods(['clientLoaded', 'mount', 'submitForm', 'uploadAudioToS3','setPatientAttributes','getFormattedDate', 'getAllPHR'], this);
      this.dataStore = new DataStore();
      this.header = new Header(this.dataStore);
      this.client = new yodaClient();
      this.clientLoaded();
    }
  
    async clientLoaded() {
      //get the URL param for identity of patient
      const urlParams = new URLSearchParams(window.location.search);
      this.dataStore.set("patientId", urlParams.get("id"));
      const providerObj = await this.client.getIdentity();
      this.dataStore.set("provider", providerObj.name);
      const patient = await this.client.getPatient(urlParams.get("id"));
      this.setPatientAttributes(patient);
      console.log(patient.name, "clientLoaded")
  
      // Initialize audio recording variables
      this.mediaRecorder = null;
      this.chunks = [];
      this.recordedAudio = null;
  
      // Get reference to record, stop, play buttons, and audio element
      this.recordButton = document.getElementById("recordVisit");
      this.stopButton = document.getElementById("stopVisit");
      this.playButton = document.getElementById("playAudio");
      this.audioElement = document.getElementById("audio_preview");
      this.submitButton = document.getElementById("submitVisit");

  
      // Attach event listeners
      this.recordButton.addEventListener("click", this.startRecording);
      this.stopButton.addEventListener("click", this.stopRecording);
      this.playButton.addEventListener("click", this.playAudio);
      this.submitButton.addEventListener("click", this.submitForm);
      window.addEventListener('apiError', function (e) {
        const alertElement = document.getElementById('alert');
        alertElement.textContent = e.detail;
        alertElement.style.display = 'block';
    }, false);
    window.addEventListener('apiError', function (e) {
      // Assuming you have an element with an ID of 'alert'
      const alertElement = document.getElementById('alert');
      const h4 = document.createElement('h4');
      h4.innerText = e.detail;
  
      const closer = document.createElement("button");
      closer.classList.add("btn","seen-btn");
      closer.id = "closeAlert";
      closer.innerText = "Ok";
     
  
      alertElement.innerHTML = '';
      
      alertElement.appendChild(h4);
      alertElement.appendChild(closer);
      alertElement.style.display = 'block';
  }, false);
  document.getElementById("alert").addEventListener("click", this.closeAlert);
}

closeAlert() {
  console.log("this");
  document.getElementById('alert').style.display = 'none';
  
}

  
    async mount() {
      this.header.addHeaderToPage();
      await this.getAllPHR(this.dataStore.get("patientId"));
    }


    getFormattedDate() {
      const currentDate = new Date();
      const year = String(currentDate.getFullYear());
      const month = String(currentDate.getMonth() + 1).padStart(2, '0');
      const day = String(currentDate.getDate()).padStart(2, '0');
      
      return `${year}-${month}-${day}`;
    }

    async setPatientAttributes(patient){
      console.log(patient.sex, "SEX")
        document.getElementById("visitName").innerText = patient.name
        document.getElementById("visitAge").innerText = "Age: " + patient.age
        const sex = document.getElementById("visitSex");
        const profilePic = document.getElementById("profilepic");
        sex.innerText ="Sex: " + patient.sex;
        if (patient.sex == 'M') {
          profilePic.src ="images/mens.png";
        } else {
          profilePic.src ="images/womens.jpg";
        }
        document.getElementById("visitAddress").innerText = "Address: " + (patient.address || "1234 Somewhere ln Nashville, TN 37206");
        document.getElementById("visitPhone").innerText = "Phone: " + (patient.phone || "555-555-5555");
        
      }

    async submitForm(event){
        event.preventDefault();

        const dateString = this.getFormattedDate();
        const patientName = this.dataStore.get("patientId");
        const providerName = this.dataStore.get("provider");
        const type = document.getElementById("dictationType").value;
        //create the phr to get the Id - this also creates dict object with the correct name to match s3 bucket filename
        const newPHR = await this.client.createPHR(this.dataStore.get("patientId"), this.dataStore.get("provider"), dateString, type)
        const idForPhr = newPHR.phr.phrId;
        //create the filename
        const filename = idForPhr;
        console.log(filename,"filename");
        const s3string = await this.client.getPresignedS3(filename, idForPhr,dateString);
        console.log(s3string.url);
        const s3response = await this.uploadAudioToS3(s3string.url);
        //make a call to updateDict object and trigger transcription
        if (s3response.status === "200") {
          const resultUpdateDictation = await this.client.updateDictation(idForPhr, dateString,filename,type);
          //do something for feedback to the user and redirect to desktop
          console.log(resultUpdateDictation,"updateDictation!!!!!");
          if (resultUpdateDictation.status == "SUCCESS") {
            let toastHTMLElement = document.getElementById('YodaSent');
            let toastElement = new bootstrap.Toast(toastHTMLElement);
            toastElement.style = "text-center"
            toastElement.show();
            this.recordButton.disabled = false;
            this.stopButton.disabled = false;
            this.playButton.disabled = true;
            this.recordedAudio = null;
            window.location.href = '/desktop.html';
          } else {
            console.log("terrible the update dictation failed: STATUS:", s3response.status);
            let toastHTMLElement = document.getElementById('YodaSentFail');
            let toastElement = new bootstrap.Toast(toastHTMLElement);
            toastElement.style = "text-center"
            toastElement.show();
            //resultUpdateDictation = await this.client.updateDictation(idForPhr, dateString,filename,type);
          }

        } else {
          console.log("terrible the update dictation failed: STATUS:", s3response.status);
          let toastHTMLElement = document.getElementById('YodaDicationFail');
          let toastElement = new bootstrap.Toast(toastHTMLElement);
          toastElement.style = "text-center"
          toastElement.show();
        }

    }

    async uploadAudioToS3(presignedS3Url) {
        if (!this.recordedAudio) {
          console.warn("No recorded audio available.");
          return;
        }
  
        // Convert recorded audio to blob 
        const response = await fetch(this.recordedAudio.src);
        const audioBlob = await response.blob();

      try {
          const uploadResponse = await this.client.dropInTheBucket(presignedS3Url, audioBlob);
          // return uploadResponse;
          return uploadResponse;
      } catch (error) {
          console.error('Error uploading audio to S3: ', error);
      }
    }

    startRecording = () => {
      const visualizer = document.getElementById("visualizer");
      visualizer.style = "display: flex;  height: 50px;";
      let toastHTMLElement = document.getElementById('YodaRecord');
      let toastElement = new bootstrap.Toast(toastHTMLElement);
      toastElement.style = "text-center"
      toastElement.show();
      navigator.mediaDevices.getUserMedia({ video: false, audio: true })
        .then((stream) => {
          this.audioElement.srcObject = stream;
          if (MediaRecorder.isTypeSupported('audio/webm')) {
            this.mediaRecorder = new MediaRecorder(stream, { mimeType: 'audio/webm' });
          } else {
            // Handle fallback if webm format is not supported
            console.log('audio/webm not supported');
            return;
          }          
          this.mediaRecorder.start();
          this.mediaRecorder.ondataavailable = this.onDataAvailable;
          this.recordButton.disabled = true;
          this.stopButton.disabled = false;
          this.playButton.disabled = true;
          this.recordedAudio = null;
        })
        .catch(this.onRecordFail.bind(this));
    }
  
    stopRecording = () => {
      const visualizer = document.getElementById("visualizer");
      visualizer.style = "display: none;  height: 50px;";
      this.mediaRecorder.stop();
      this.mediaRecorder = null;
      this.recordButton.disabled = false;
      this.stopButton.disabled = true;
      this.playButton.disabled = false;
  
      setTimeout(() => {
      const audioBlob = new Blob(this.chunks, { type: "audio/ogg" });
      const audioURL = window.URL.createObjectURL(audioBlob);
      this.recordedAudio = new Audio(audioURL);
      this.chunks = [];
      }, 100);
  };
      
  
    playAudio = () => {
      if (this.recordedAudio) {
        this.recordedAudio.play();
      } else {
        console.warn("No recorded audio available.");
      }
    }
    
    stopRecordedAudio = () => {
      this.recordButton.disabled = false;
      this.stopButton.disabled = true;
      this.playButton.disabled = false;
      if (this.recordedAudio) {
        this.recordedAudio.stop();
      }

    }

    // Handle available audio data
    onDataAvailable = (event) => {
      this.chunks.push(event.data);
    }
  
    // Handle recording failure
    onRecordFail(e) {
      console.log(e);
    }

    //Add all PHR's in Dynamo  for review
    async getAllPHR(patientName) {
      const accordion = document.getElementById('phrAccordion');
      const result = await this.client.getAllPHR(patientName);
      result.phrId.reverse();
      if (result.phrId.length > 0) {
        result.phrId.forEach((phr, index) => {
            const accordionItem = document.createElement('div');
            accordionItem.classList.add('accordion-item');
    
            const accordionHeader = document.createElement('h2');
            accordionHeader.classList.add('accordion-header');
            accordionHeader.id = `heading${index+1}`;
    
            const accordionButton = document.createElement('button');
            accordionButton.classList.add('accordion-button', 'collapsed');
            accordionButton.type = 'button';
            accordionButton.dataset.bsToggle = 'collapse';
            accordionButton.dataset.bsTarget = `#collapse${index+1}`;
            accordionButton.setAttribute('aria-expanded', 'false');
            accordionButton.setAttribute('aria-controls', `collapse${index+1}`);
    
            const dateDiv = document.createElement('div');
            dateDiv.classList.add('attr', 'date');
            dateDiv.innerText = `DATE: ${phr.date}`;
            accordionButton.appendChild(dateDiv);
    
            const statusDiv = document.createElement('div');
            statusDiv.classList.add('attr', 'status');
            statusDiv.innerText = `STATUS: ${phr.status}`;
            accordionButton.appendChild(statusDiv);
    
            const idDiv = document.createElement('div');
            idDiv.classList.add('attr', 'id');
            idDiv.innerText = `ID: ${phr.phrId}`;
            accordionButton.appendChild(idDiv);
    
            const accordionCollapse = document.createElement('div');
            accordionCollapse.id = `collapse${index+1}`;
            accordionCollapse.classList.add('accordion-collapse', 'collapse');
            accordionCollapse.setAttribute('aria-labelledby', `heading${index+1}`);
            accordionCollapse.dataset.bsParent = '#phrAccordion';
            

            const accordionBody = document.createElement('div');
            accordionBody.classList.add('accordion-body');
            accordionBody.innerHTML = `
            <p>Patient ID: ${phr.patientId}</p>
            <p>Provider Name: ${phr.providerName}</p>
            <p>Date: ${phr.date}</p>
            <p>Status: ${phr.status}</p>
            `;
            if (phr.comprehendData != null) {
              this.client.parseComp(phr.comprehendData)
                .then(additionToAccordion => {
                  accordionBody.appendChild(additionToAccordion);
                })
                .catch(error => {
                  console.error('Error parsing compData:', error);
                });
            }
                
            accordionHeader.appendChild(accordionButton);
            accordionCollapse.appendChild(accordionBody);
            accordionItem.appendChild(accordionHeader);
            accordionItem.appendChild(accordionCollapse);
            accordion.appendChild(accordionItem);
        });
      }else {
        const noResultsMessage = document.createElement('p');
        noResultsMessage.innerText = 'No PHR\'s Found for the search criteria';
        accordion.appendChild(noResultsMessage);
      }
    }
    
    
  }


  /**
   * Main method to run when the page contents have loaded.
   */
  const main = async () => {
    const visit = new Visit();
    visit.mount();
  };
  
  window.addEventListener('DOMContentLoaded', main);
  
