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
        console.log(phr[0],"phr passed");
        const accordionBody = document.getElementById('previousAccordion');
        if (phr.comprehendData != null) {
            this.client.parseComp(phr.comprehendData)
            .then(additionToAccordion => {
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
  
