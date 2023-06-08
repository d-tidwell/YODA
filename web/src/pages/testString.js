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
        this.patientsNullable = null;
    }

    async clientLoaded(){
        const identity =  await this.client.getIdentity();
        const provider =  await this.client.getProvider(identity.name);
        this.populatePatientsPending(provider);
        this.populatePhrPending(provider);
        
    }
    /**
     * Add the header to the page and load the Client.
     */
    mount() {

        this.header.addHeaderToPage();

        this.client = new yodaClient();
       
        this.clientLoaded();

         // Get the Patients tab button
         let patientTab = document.getElementById('nav-profile-tab-button');

         // Add event listener for 'click' event
         patientTab.addEventListener('click', () => {
             // Call your function here
             console.log("Patients tab clicked!");
             this.getAllPatientsAndDisplay();
         });

        const createPatientSubmitButton = document.getElementById('createPatientSubmit');
        createPatientSubmitButton.addEventListener('click', (event) => this.createPatient(event));
        const phrSearchSubmitButton = document.getElementById('submitPHRSearch');
        phrSearchSubmitButton.addEventListener('click', (event) => this.searchPHR(event));

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
    
        console.log(response,"patient pendings");
    }
    
    
    async populatePatientsPending(provider){
        console.log(provider)
        var listGroup = document.getElementById('desktopListGroupPatients');
        const identity = await this.client.getIdentity();
        const self = this; // add this line
        for (const patient of provider.pendingPatients) {
            try {
                var listItem = document.createElement('li');
                listItem.className = 'list-group-item d-flex justify-content-between align-items-center';
                listItem.innerHTML = `<h4>Loading ...<h4>`
        
                // add await before this.client.getPatient(patient)
                const patientName = await this.client.getPatient(patient);
        
                listItem.innerHTML = `
                    ${patientName.name}
                    <div>
                    <button class="btn btn-primary visit-btn">Visit</button>
                    <button class="btn btn-primary seen-btn">Seen</button>
                    </div>
                `;
        
                listGroup.appendChild(listItem);
        
                (function(self, listItem, patient) { // modify this line
                    listItem.querySelector('.visit-btn').addEventListener('click', function() {
                        window.open('/visit.html?id=' + patient, '_blank');
                    });
        
                    listItem.querySelector('.seen-btn').addEventListener('click', async () => {
                        const result = await self.client.removePatient(patient, identity.name); // modify this line
                        console.log(result.success, "test");
                        if (result.success === true) {
                            listItem.remove();  
                        }
                    });
                })(self, listItem, patient); // modify this line
            } catch (err) {
                console.log(err);  // handle errors here
            }
        }
    }
    
    
    
    
    async getAllPatientsAndDisplay() {
        try {
            // check the patientsNullable
            if (this.patientsNullable == null){
                const patients = await this.client.getAllPatients();
                this.patientsNullable = 1;
            
         
                // Make sure the patients data is an array
                console.log(patients, "here");
        
                // Get the identity
                const identity = await this.client.getIdentity();
        
                // Get a reference to the list element
                const list = document.getElementById('allPatientsList');
        
                // Iterate over the array of patients
                for (let patient of patients.patients) {
                    console.log(patient,"loop");
        
                    // Create a new list item for each patient
                    let li = document.createElement('li');
                    li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center');
                    
                    // Create text node for patient name and age
                    let textNode = document.createTextNode(`Name: ${patient.name}, Age: ${patient.age}`);
                    li.appendChild(textNode);
                    
                    // Create the button container
                    let buttonContainer = document.createElement('div');
        
                    // Create the Add button
                    let addButton = document.createElement('button');
                    addButton.classList.add('btn', 'btn-primary');
                    addButton.textContent = 'Add';
                    addButton.style.marginRight = '4px';
        
                    // Add event listener to Add button
                    addButton.addEventListener('click', async () => {
                        console.log("clicked")
                        const confirm = await this.client.addPatientToProvider(patient.id, identity.name);
                        if(confirm.success == true){
                            location.reload();
                        }
                    });
                    buttonContainer.appendChild(addButton);
        
                    // Create the Edit button
                    let editButton = document.createElement('button');
                    editButton.classList.add('btn', 'btn-primary');
                    editButton.textContent = 'Edit';
                    editButton.id = 'allPatientsEdit'; // Change id of the Edit button
                    
                    // Add event listener to Edit button
                    editButton.addEventListener('click', () => {
                        window.open("/editPatient.html?id=" + patient.id, "_blank");
                    });
                    buttonContainer.appendChild(editButton);
                    
                    // Append the button container to the list item
                    li.appendChild(buttonContainer);
                    
                    // Append the new list item to the list
                    list.appendChild(li);
                }
            }
            } catch(err) {
                // Handle any errors
                console.error('An error occurred:', err);
            }
        }
        
        

    createPatient(event) {
        // Prevent the form from being submitted normally
        event.preventDefault();
    
        // Get the form values
        const name = document.getElementById('patientName').value;
        const age = document.getElementById('patientAge').value;
    
        // Call createPatient() method
        this.client.createPatient(name, age)
            .then(() => {
                // The patient was created successfully
                console.log('Patient created successfully');
                
                // Clear the form fields
                document.getElementById('patientName').value = '';
                document.getElementById('patientAge').value = '';
                document.getElementById('patientAddress').value = '';
                document.getElementById('patientPhoneNumber').value = '';
                document.getElementById('patientImage').value = '';
    
                // Refresh the list of patients
                this.getAllPatientsAndDisplay();
                
            })
        .catch((err) => {
            // An error occurred
            console.error('An error occurred:', err);
        });
    }

    async searchPHR(event) {
        event.preventDefault();
    
        // Get the input values from the form
        const phrIdInput = document.getElementById('phrId').value.trim();
        const phrDateInput = document.getElementById('phrDate').value;
        const fromDateInput = document.getElementById('fromDate').value;
        const toDateInput = document.getElementById('toDate').value;
        const patientIdInput = document.getElementById('patientId').value.trim();  // New patient ID input
    
        let phrResultsArray = [];
    
        // Get a reference to the accordion and clear any existing results
        const accordion = document.getElementById('phrAccordion');
        accordion.innerHTML = "";
    
        // Decide which function to call based on inputs provided
        let result;
        if (patientIdInput !== "" && phrIdInput === "" && phrDateInput === "" && fromDateInput === "" && toDateInput === "") {
            // If only patientIdInput is provided, call getPHR with patientIdInput
            result = await this.client.getAllPHR(patientIdInput);
        } else if (phrIdInput !== "") {
            result = await this.client.getPHR(phrIdInput);
        } else if (phrDateInput !== "" && patientIdInput !== "") {
            // For date or date range search, patientIdInput must be provided
            result = await this.client.getPHRDate(patientIdInput, phrDateInput);
        } else if (fromDateInput !== "" && toDateInput !== "" && patientIdInput !== "") {
            // For date or date range search, patientIdInput must be provided
            result = await this.client.getPHRDateRange(patientIdInput, fromDateInput, toDateInput);
        }
        console.log(result,"results")
        // Check if the result is an array. If so, assign it directly to phrResultsArray. If not, wrap it in an array.
        phrResultsArray = result && Array.isArray(result.phrId) ? result.phrId : [];

        console.log(phrResultsArray, "after check for array")
        // Now the results are in phrResultsArray, which can be iterated over to populate the accordion
        if (phrResultsArray.length > 0) {
            phrResultsArray.forEach((phr, index) => {
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
                // !Change This
                accordionButton.innerText = `PHR ${phr.date}`;
    
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
        document.getElementById('phrId').value = '';
        document.getElementById('phrDate').value = '';
        document.getElementById('fromDate').value = '';
        document.getElementById('toDate').value = '';
        document.getElementById('patientId').value = '';
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
