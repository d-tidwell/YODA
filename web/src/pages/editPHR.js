import yodaClient from '../api/yodaClient';
import BindingClass from "../util/bindingClass";
import Header from '../components/header';
import DataStore from "../util/DataStore";

class EditPHR extends BindingClass {
    constructor() {
      super();
      this.bindClassMethods(['clientLoaded', 'mount', 'getFormattedDate','createEditablePHR'], this);
      this.dataStore = new DataStore();
      this.header = new Header(this.dataStore);
      this.client = new yodaClient();
      this.clientLoaded();
    }
  
    async clientLoaded() {
      //get the URL param for identity of patient
      const urlParams = new URLSearchParams(window.location.search);
      this.dataStore.set("phrId", urlParams.get("phrId"));
      console.log(this.dataStore.get("phrId"));
      const providerObj = await this.client.getIdentity();
      console.log(providerObj,"Prov");
      this.dataStore.set("provider", providerObj.name);
      //!!! return to normal
    //   const phr = await this.client.getPHR(urlParams.get("phrId"));
      const phr = await this.client.getPHR("postVisit_TEST_PATIENT1_2023-06-10_0000000000");
      const patient = await this.client.getPatient(phr.patientId);
      console.log(patient,"patient");
      this.setPatientAttributes(patient);
      this.createEditablePHR(phr);
  
  
    //   // Get reference to record, stop, play buttons, and audio element
    //   this.submitButton = document.getElementById("submitVisit");

  
    //   // Attach event listeners
    //   this.submitButton.addEventListener("click", this.submitForm);
  
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
      // document.getElementById("visitAddress").innerHtml = "Address " +patient.address;
      // document.getElementById("visitPhone").innerHtml = "Phone " + patient.phone;
    }

    async createEditablePHR(phr){
        // const buttonsDiv = document.createElement("div");
        // const signatureBtn = document.createElement("button");
        // signatureBtn.classList.add("btn","seen-btn");
        // signatureBtn.innerHTML = "Signature";
        // buttonsDiv.appendChild(signatureBtn);
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
        this.dataStore.set("transcription",phr.transcription);
        if (phr.comprehendData != null) {
            this.client.editCompForm(phr.comprehendData)
            .then(additionToAccordion => {
                const dictationDiv = document.createElement('div');
                dictationDiv.className = "headingBoxPHR";
                const img = document.createElement("img");
                img.src = "/images/Icons/hippo.png";
                img.className="iconography-phr-result";
                dictationDiv.appendChild(img);
                const headingDict = document.createElement("h5");
                headingDict.innerHTML ="TEXT FROM AUDIO";
                headingDict.style="color:white";
                dictationDiv.appendChild(headingDict);
                const dictationForm = document.createElement('form');
                const dictationLabel = document.createElement('label');
                dictationLabel.for = `patientNotes-${self.dataStore.get("phrId")}`;
                const dictationText = document.createElement('textArea');
                dictationText.value = self.dataStore.get("transcription");
                dictationText.classList.add("form-control");
                dictationText.id = `patientNotes-${phr.patientID}`;
                dictationText.rows = "10";
                dictationText.columns = "12"

                accordionBody.appendChild(dictationDiv);
                accordionBody.appendChild(dictationForm);
                accordionBody.appendChild(dictationLabel);
                accordionBody.appendChild(dictationText);
                accordionBody.appendChild(additionToAccordion);
            })
            .catch(error => {
                console.error('Error parsing compData', error);
            });
    }
    // async submitForm(event){
    //     event.preventDefault();

    //     return 0;
    // }

 
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
  
