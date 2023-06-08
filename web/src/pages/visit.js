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
  
    }
  
    async mount() {
      this.header.addHeaderToPage();
      const loggedIn = await this.client.isLoggedIn();
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
      document.getElementById("visitName").innerText = patient.name
      document.getElementById("visitAge").innerText = "Age: " + patient.age
   }

    async submitForm(event){
        event.preventDefault();

        const dateString = this.getFormattedDate();
        const patientName = this.dataStore.get("patientId");
        const providerName = this.dataStore.get("provider");
        const type = document.getElementById("dictationType").value;
        //!!!!create the phr to get the Id

        //create the filename
        const filename = dateString +"-"+patientName+"-"+providerName+"-"+type;

        console.log(filename,"filename");
        const s3string = await this.client.getPresignedS3(filename, "IDNONEXISTENT",dateString);
        console.log(s3string.url);
        this.uploadAudioToS3(s3string.url);
        //!!!!make a call to create the dictation object & populate the data passing the phrid

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
          console.log(uploadResponse);
      } catch (error) {
          console.error('Error uploading audio to S3: ', error);
      }
    }
      
    startRecording = () => {
      const supportedAudioFormats = [
        'audio/wav',
        'audio/webm',
        'audio/ogg',
        'audio/mpeg',
        // Add more supported MIME types as needed
      ];
      
      const supportedFormats = supportedAudioFormats.filter((format) => MediaRecorder.isTypeSupported(format));
      
      console.log('Supported Audio Formats:', supportedFormats);
      navigator.mediaDevices.getUserMedia({ video: false, audio: true })
        .then((stream) => {
          this.audioElement.srcObject = stream;
          if (MediaRecorder.isTypeSupported('audio/webm')) {
            this.mediaRecorder = new MediaRecorder(stream, { mimeType: 'audio/webm' });
          } else {
            // Handle fallback if WAV format is not supported
            console.log('audio/wav not supported');
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
            <p>Dictation ID: ${phr.dictationId}</p>
            `; 
    
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
  
