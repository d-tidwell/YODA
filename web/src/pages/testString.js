import yodaClient from '../api/yodaClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/*
The code below this comment is equivalent to...
const EMPTY_DATASTORE_STATE = {
    'search-criteria': '',
    'search-results': [],
};

...but uses the "KEY" constants instead of "magic strings".
The "KEY" constants will be reused a few times below.
*/

const SEARCH_CRITERIA_KEY = 'search-criteria';
const SEARCH_RESULTS_KEY = 'search-results';
const EMPTY_DATASTORE_STATE = {
    [SEARCH_CRITERIA_KEY]: '',
    [SEARCH_RESULTS_KEY]: [],
};


/**
 * Logic needed for the view pages of the website.
 */
class TestString extends BindingClass {
    constructor() {
        super();

        this.bindClassMethods(['mount','populatePatientsPending','clientLoaded','populatePhrPending','getAllPatientsAndDisplay'], this);

        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
        this.dataStore.addChangeListener(this.displaySearchResults);
    }

    async clientLoaded(){
        const identity =  await this.client.getIdentity();
        const provider =  await this.client.getProvider(identity.name);
        this.populatePatientsPending(provider);
        this.populatePhrPending(provider);
        
        // Get the Patients tab button
        let patientTab = document.getElementById('nav-profile-tab-button');

        // Add event listener for 'click' event
        patientTab.addEventListener('click', () => {
            // Call your function here
            console.log("Patients tab clicked!");
            this.getAllPatientsAndDisplay();
        });
    }
    /**
     * Add the header to the page and load the Client.
     */
    mount() {

        this.header.addHeaderToPage();

        this.client = new yodaClient();
       
        this.clientLoaded();

    }


    async populatePhrPending(provider) {
        const response = await this.client.getAllPHRByProvider(provider.name);

        const phrList = document.getElementById('phrPendingList');
    
        phrList.innerHTML = '';
    
        // For each PHR, generate HTML and append to the list
        for (const phrId of response.phrId) {
            // Use getPatient() method to fetch patient details
            const patient = await this.client.getPatient(phrId.patientId);
            
            // Create a list item for each PHR
            let li = document.createElement('li');
            li.className = 'list-group-item d-flex justify-content-between align-items-center';

            let idDiv = document.createElement('div');
            idDiv.style.marginRight = '10px'; // adjust as needed
            let textNode = document.createTextNode(`id: ${phrId.phrId}`);
            idDiv.appendChild(textNode);
            li.appendChild(idDiv);

            let nameDiv = document.createElement('div');
            nameDiv.style.marginRight = '10px'; // adjust as needed
            let textNode2 = document.createTextNode(`Name: ${patient.name}`);
            nameDiv.appendChild(textNode2);
            li.appendChild(nameDiv);

            let statusDiv = document.createElement('div');
            let statusTextNode = document.createTextNode(`Status: ${phrId.status}`);
            statusDiv.appendChild(statusTextNode);
            li.appendChild(statusDiv);

            let buttonsDiv = document.createElement('div');
            let signButton = document.createElement('button');
            signButton.className = 'btn btn-primary';  
            signButton.innerText = 'Sign';
            signButton.style.marginRight = '4px';
            buttonsDiv.appendChild(signButton);
        
            let editButton = document.createElement('button');
            editButton.className = 'btn btn-secondary';
            editButton.innerText = 'Edit';
            
            // Add an event listener to the Edit button
            editButton.addEventListener('click', function() {
                // Open a new tab and pass phrId as a URL parameter
                window.open(`editPHR.html?phrId=${phrId.phrId}`, '_blank');
            });
    
            buttonsDiv.appendChild(editButton);
    
            li.appendChild(buttonsDiv);
    
            phrList.appendChild(li);
        }
    
        console.log(response);
    }
    
    
    async populatePatientsPending(provider){

        var listGroup = document.getElementById('desktopListGroupPatients');
    
        for (const patient of provider.pendingPatients) {
            try {
                var listItem = document.createElement('li');
                listItem.className = 'list-group-item d-flex justify-content-between align-items-center';
                listItem.innerHTML = `<h4>Loading ...<h4>`
                const patientName = await this.client.getPatient(patient);
    
                listItem.innerHTML = `
                    ${patientName.name}
                    <div>
                    <button class="btn btn-primary visit-btn">Visit</button>
                    <button class="btn btn-primary seen-btn">Seen</button>
                    </div>
                `;
    
                listGroup.appendChild(listItem);
    
                listItem.querySelector('.visit-btn').addEventListener('click', function() {
                    window.open('/visit.html?id=' + patient, '_blank');
                });
    
                listItem.querySelector('.seen-btn').addEventListener('click', () => {
                    this.client.removePatient(patient, identity.name);
                    if (result === true) {
                        listItem.remove();  
                    }
                });
            } catch (err) {
                console.log(err);  // handle errors here
            }
        }
    }
    
    async getAllPatientsAndDisplay() {
        // make the API call
        this.client.getAllPatients()
          .then(patients => {
            // make sure the patients data is an array
            console.log(patients, "here")
              // get a reference to the list element
              const list = document.getElementById('allPatientsList');
      
              // iterate over the array of patients
              for (let patient of patients.patients) {
                console.log(patient,"loop")
                // create a new list item for each patient
                let li = document.createElement('li');
                li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center');
                
                // create text node for patient name and age
                let textNode = document.createTextNode(`Name: ${patient.name}, Age: ${patient.age}`);
                li.appendChild(textNode);
                
                // create the button container
                let buttonContainer = document.createElement('div');
      
                // create the Add button
                let addButton = document.createElement('button');
                addButton.classList.add('btn', 'btn-primary');
                addButton.textContent = 'Add';
                addButton.style.marginRight = '4px';
                buttonContainer.appendChild(addButton);
      
                // create the Edit button
                let editButton = document.createElement('button');
                editButton.classList.add('btn', 'btn-primary');
                editButton.textContent = 'Edit';
                buttonContainer.appendChild(editButton);
                
                // append the button container to the list item
                li.appendChild(buttonContainer);
                
                // append the new list item to the list
                list.appendChild(li);
              }
          })
          .catch(err => {
            // handle any errors
            console.error('An error occurred:', err);
          });
      }
      
      

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const testString = new TestString();
    testString.mount();
};

window.addEventListener('DOMContentLoaded', main);
