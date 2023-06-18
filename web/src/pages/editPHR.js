import yodaClient from '../api/yodaClient';
import BindingClass from "../util/bindingClass";
import Header from '../components/header';
import DataStore from "../util/DataStore";

class EditPHR extends BindingClass {
    constructor() {
      super();
      this.bindClassMethods(['clientLoaded', 'mount', 'getFormattedDate','createEditablePHR','editForm','submitForm'], this);
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
      this.dataStore.set("audio", await this.client.getPresignedAudio("2023-06-05-TEST_PATIENT3-Dr.Yelyzaveta-preVisit"))
      const providerObj = await this.client.getIdentity();
      this.dataStore.set("provider", providerObj.name);
      //!!! return to normal
    //   const phr = await this.client.getPHR(urlParams.get("phrId"));
      const phr = await this.client.getPHR("postVisit_TEST_PATIENT1_2023-06-10_IJLY3XU4LV");
      this.dataStore.set("patientId", phr.patientId);
      const patient = await this.client.getPatient(phr.patientId);
      this.setPatientAttributes(patient);
      this.createEditablePHR(phr);
  
  


  
    }
  
    async mount() {
      this.header.addHeaderToPage();
      const loggedIn = await this.client.isLoggedIn();
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
        const sex = document.getElementById("visitSex");
        if (patient.sex == 'M') {
          sex.src ="images/men.png";
        } else {
          sex.src ="images/womens.png";
        }
        document.getElementById("visitAddress").innerHtml = "Address " +patient.address;
        document.getElementById("visitPhone").innerHtml = "Phone " + patient.phone;
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
        console.log(phr.transcription)
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
                console.log(dictationText.value,"dic text value recorded");
                dictationText.classList.add("form-control");
                dictationText.id = `patientNotes-${self.dataStore.get("phrId")}`;
                console.log("patient id text area id ", self.dataStore.get("patientID"));
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
                editBtn.id = "edit";
                      // Get reference to record, stop, play buttons, and audio element


  
                 // Attach event listeners
                signatureBtn.addEventListener("click", self.submitForm);
                editBtn.addEventListener("click", self.editForm);
                buttonsDiv.appendChild(signatureBtn);
                buttonsDiv.appendChild(editBtn);
                const titleDiv = document.createElement("div");
                titleDiv.classList.add("row","text-center");
                const titleComp = document.createElement("h5");
                titleComp.style.textAlign = "center";
                titleComp.innerText = "COMPREHENSION"
                titleDiv.appendChild(titleComp);
                accordionBody.appendChild(buttonsDiv);
                accordionBody.appendChild(titleDiv);
                accordionBody.appendChild(additionToAccordion);
            })
            .catch(error => {
                console.error('Error parsing compData', error);
            });
    }
  }
    async submitForm(event){
        event.preventDefault();
        const updated = this.client.updatePHRStatus(phrId, "COMPLETED");
        console.log(updated);
    }

    async editForm(event) {
      event.preventDefault();
      const text = document.getElementById(`patientNotes-${this.dataStore.get("phrId")}`);
      console.log(this.dataStore.get('patientId'), "recorded ID")
      const returnText = await this.client.editPHR(this.dataStore.get("phrId"), text.value);
      this.createEditablePHR(await this.client.get(this.dataStore.get("phrId")));
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
  
