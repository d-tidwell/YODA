import yodaClient from '../api/yodaClient';
import BindingClass from "../util/bindingClass";
import Header from '../components/header';
import DataStore from "../util/DataStore";

class EditPHR extends BindingClass {
    constructor() {
      super();
      this.bindClassMethods(['clientLoaded', 'mount', 'closeAlert','getFormattedDate','createEditablePHR','editForm','submitForm'], this);
      this.dataStore = new DataStore();
      this.header = new Header(this.dataStore);
      this.client = new yodaClient();
      this.clientLoaded();
    }
  
    async clientLoaded() {
      //get the URL param for identity of patient
      const urlParams = new URLSearchParams(window.location.search);
      this.dataStore.set("phrId", urlParams.get("phrId"));
      //!!change the hardcode audio url phrId
      this.dataStore.set("audio", await this.client.getPresignedAudio(this.dataStore.get("phrId")))
      const providerObj = await this.client.getIdentity();
      this.dataStore.set("provider", providerObj.name);
      //!!! return to normal
      const phr = await this.client.getPHR(urlParams.get("phrId"));
      this.dataStore.set("patientId", phr.patientId);
      const patient = await this.client.getPatient(phr.patientId);
      this.dataStore.set("date", phr.date);
      this.setPatientAttributes(patient);
      this.createEditablePHR(phr);
      console.log(phr,"EDITABLE RESULT")
  
    }
  
    async mount() {
      this.header.addHeaderToPage();
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
        const profilePic = document.getElementById("patientpic");
        sex.innerText ="Sex: " + patient.sex;
        if (patient.sex == 'M') {
          profilePic.src ="images/mens.png";
        } else {
          profilePic.src ="images/womens.jpg";
        }
        document.getElementById("visitAddress").innerText = "Address: " + (patient.address || "1234 Somewhere ln Nashville, TN 37206");
        document.getElementById("visitPhone").innerText = "Phone: " + (patient.phone || "555-555-5555");
        
      }

    async createEditablePHR(phr){
        let self = this;
        const accordionBody = document.getElementById('previousAccordion');
        accordionBody.classList.add('accordion-body');
        accordionBody.innerHTML = `
        <p>Patient ID: ${phr.patientId}</p>
        <p>Provider Name: ${phr.providerName}</p>
        <p>Date: ${phr.date}</p>
        <p>Status: ${phr.status}</p>
        <p>ID: ${this.dataStore.get("phrId")}
        `;
        //console.log(phr.transcription)
        this.dataStore.set("transcription",phr.transcription);
        if (phr.comprehendData != null) {
            this.client.editCompForm(phr.comprehendData)
            .then(additionToAccordion => {
                const dictationDiv = document.createElement('div');
                dictationDiv.className = "headingBoxPHR";
                const audioPreview = document.createElement("audio");
                audioPreview.id ="audioPreview";
                const audioUrl = this.dataStore.get("audio")
                audioPreview.src = audioUrl.url;
                //console.log(audioUrl.url,"url")
                audioPreview.type="audio/webm";
                audioPreview.controls ="true";
                const img = document.createElement("img");
                img.src = "/images/Icons/hippo.png";
                img.className="iconography-phr-result";
                dictationDiv.appendChild(img);
                const headingDict = document.createElement("h5");
                headingDict.innerHTML ="AUDIO CAPTURE";
                headingDict.style="color:white";
                dictationDiv.appendChild(headingDict);
                dictationDiv.appendChild(audioPreview);
                const dictationForm = document.createElement('form');
                dictationForm.innerText = "TRANSCRIPTION RESULTS"
                const dictationLabel = document.createElement('label');
                dictationLabel.for = `patientNotes-${self.dataStore.get("phrId")}`;
                const dictationText = document.createElement('textArea');
                dictationText.value = self.dataStore.get("transcription");
                //console.log(dictationText.value,"dic text value recorded");
                dictationText.classList.add("form-control");
                dictationText.id = `patientNotes-${self.dataStore.get("phrId")}`;
                //console.log("patient id text area id ", self.dataStore.get("patientID"));
                dictationText.rows = "10";
                dictationText.columns = "12"
                accordionBody.appendChild(dictationDiv);
                accordionBody.appendChild(dictationForm);
                accordionBody.appendChild(dictationLabel);
                accordionBody.appendChild(dictationText);
                const buttonsDiv = document.createElement("div");
                buttonsDiv.style.display = "flex";
                buttonsDiv.classList.add("py-3","text-center");
                const signatureBtn = document.createElement("button");
                signatureBtn.classList.add("btn","seen-btn");
                signatureBtn.innerHTML = "Signature";
                signatureBtn.style.flex = "1";
                signatureBtn.id = "sign";
                
                const editBtn = document.createElement("button");
                editBtn.classList.add("btn","seen-btn");
                editBtn.innerHTML = "edit";
                editBtn.style.flex = "1";
                editBtn.id = "edits";
                      // Get reference to record, stop, play buttons, and audio element


  
                 // Attach event listeners
                signatureBtn.addEventListener("click", () => self.submitForm(this.dataStore.get("phrId")));
                editBtn.addEventListener("click", (event) => self.editForm(event));

                buttonsDiv.appendChild(signatureBtn);
                buttonsDiv.appendChild(editBtn);
                const modelDiv = document.createElement("div");
                modelDiv.classList.add("headingBoxPHR");
                modelDiv.classList.add("row","text-center");
                modelDiv.style.background ="#ffc160";
                
                const modelComp = document.createElement("h3");
                modelComp.style = "text-center";
                modelComp.style.color ="white";
                modelComp.innerText = "Ai Assistant";
                
                const openDiv = document.createElement("div");
                
                const modelResult = document.createElement("textarea");
                modelResult.style = "visibility: hidden; width: 100%; resize: both;"; // added styles for full width and resizable
                
                const aiButton = document.createElement("button");
                aiButton.style = "width: 100%; margin-bottom: 10px;"; // full width button with some margin at the bottom
                aiButton.id = "differential"
                aiButton.innerText = "Request Differential Diagnosis";
                aiButton.style.background = "#f4befb"
                
                aiButton.addEventListener("click", function() {
                    modelResult.style.visibility = "visible";  // correct property is 'visible'
                    openDiv.innerText = "Generating Differential with the force, I am . . . Please Wait, You should.";
                
                    const phrId = self.dataStore.get("phrId");
                    const date = self.dataStore.get("date");
                
                    self.client.differential(phrId, date).then(async (result) => {
                      if (result && result.hasOwnProperty('differential')) {
                          modelResult.value = result.differential;
                          openDiv.innerText = "";
                          console.log(result);
                      } else {
                          throw new Error("No differential in result");
                      }
                  }).catch(error => {
                      console.log('Error:', error);
                      openDiv.innerText = "Error occurred. Please try again.";
                      modelResult.style.visibility = "hidden";  // hide the modelResult again
                  });
                  
                });
                
                modelDiv.appendChild(modelComp); 
                modelDiv.appendChild(openDiv);
                modelDiv.appendChild(modelResult);
                modelDiv.appendChild(aiButton);
                
                const titleDiv = document.createElement("div");
                titleDiv.classList.add("row","text-center");
                const titleComp = document.createElement("h5");
                titleComp.style.textAlign = "center";
                titleComp.innerText = "COMPREHENSION"
                titleDiv.appendChild(titleComp);
                accordionBody.appendChild(buttonsDiv);
                accordionBody.appendChild(modelDiv);
                accordionBody.appendChild(titleDiv);
                accordionBody.appendChild(additionToAccordion);
                
            })
            .catch(error => {
                console.error('Error parsing compData', error);
            });
    } else {
      const needBtn = document.getElementById("previousAccordion");
      const clostBtn = document.createElement("button");
      clostBtn.id = "closer";
      clostBtn.classList.add("btn","seen-btn");
      clostBtn.innerText ="CLOSE OUT PHR";
      clostBtn.addEventListener('click', async (event) => {
        await this.submitForm(this.dataStore.get("phrId"), event);
        window.location.reload();
      });
      needBtn.appendChild(clostBtn);
    }
  }
    async submitForm(phrId){
        const updated = await this.client.updatePHRStatus(phrId, "COMPLETED");
        console.log("updated",updated);
        await this.createEditablePHR(await this.dataStore.get("phrId"));
        window.location.reload();
    }

    async editForm(event) {
      event.preventDefault();
      const text = await document.getElementById(`patientNotes-${this.dataStore.get("phrId")}`);
      console.log(await this.dataStore.get('patientId'), "recorded ID")
      const returnText = await this.client.editPHR(this.dataStore.get("phrId"), text.value);
      await this.createEditablePHR(await this.dataStore.get("phrId"));
      window.location.reload();
    }
 
  }

  /**
   * Main method to run when the page contents have loaded.
   */
  const main = async () => {
    const editPHR = new EditPHR();
    editPHR.mount();
  };
  
  window.addEventListener('DOMContentLoaded', main);
  
