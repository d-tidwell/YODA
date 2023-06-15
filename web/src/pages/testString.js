import yodaClient from '../api/yodaClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";


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
        this.client = new yodaClient();
    }
    
    async clientLoaded(){
    
        
        let provider;
        const identity =  await this.client.getIdentity();
        
        try {
            console.log("making request", identity.name);
            provider = await this.client.getProvider("Dr."+ identity.name);
            console.log(provider,"here")
        } catch (error) {
            console.log(identity,"couldn't find the provider");
            let toastHTMLElement = document.querySelector('.toast');
            let toastElement = new bootstrap.Toast(toastHTMLElement);
            toastElement.style = "text-center"
            toastElement.show();
            let created = await this.client.createProvider(identity.name, identity.email);
            provider = await this.client.getProvider("Dr."+identity.name);
            console.log(provider,"there");
            toastHTMLElement.addEventListener('hidden.bs.toast', function () {
                toastHTMLElement.parentNode.removeChild(toastHTMLElement);
              });
        }
        let toastHTMLElement2 = document.getElementById('YodaSuccess');
        let toastElement2 = new bootstrap.Toast(toastHTMLElement2);
        toastElement2.style = "text-center";
        toastElement2.show();
       

        await this.populatePatientsPending(provider);
        await this.populatePhrPending(provider.name);

        }
        

    /**
     * Add the header to the page and load the Client.
     */
    mount() {

        this.clientLoaded();
        this.header.addHeaderToPage();

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


    async populatePhrPending(providerName) {
        const response = await this.client.getAllPHRByProvider(providerName);
        const responseId = response.phrId;
        const phrList = document.getElementById('phrPendingList');
    
        phrList.innerHTML = '';

        if(responseId){
            for (const phrId of responseId) {
                const labelStyle = 'width: 80px; font-weight: bold;';
                const valueStyle = 'margin-left: 5px; margin-right:20px'; 
                // Use getPatient() method to fetch patient details
                const patient = await this.client.getPatient(phrId.patientId);

                // Create a list item for each PHR
                let li = document.createElement('li');
                li.className = 'list-group-item d-flex flex-wrap'; // Add flex-wrap to allow wrapping of content

                // Create container for phrID and buttons
                let phrContainer = document.createElement('div');
                phrContainer.className = 'phr-container d-flex justify-content-between align-items-center flex-grow-1';

                let idContainer = document.createElement('div');
                idContainer.className = 'label-value-container';
                let idLabel = document.createElement('span');
                idLabel.style.cssText = labelStyle;
                idLabel.innerText = 'Id:';
                let idValue = document.createElement('span');
                idValue.style.cssText = valueStyle; // Apply the value style
                idValue.innerText = phrId.phrId;


                // Create buttons container and append buttons
                let buttonsDiv = document.createElement('div');
                buttonsDiv.className = '';
    

                let signButton = document.createElement('button');
                signButton.className = 'btn seen-btn';
                signButton.innerText = 'Sign';
                signButton.style.marginRight = '4px';
                buttonsDiv.appendChild(signButton);

                let editButton = document.createElement('button');
                editButton.className = 'btn seen-btn';
                editButton.innerText = 'Edit';

                // Add an event listener to the Edit button
                editButton.addEventListener('click', function () {
                    // Open a new tab and pass phrId as a URL parameter
                    window.open(`editPHR.html?phrId=${phrId.phrId}`, '_blank');
                });

                buttonsDiv.appendChild(editButton);

                // Append phrID, buttons, and buttons container to the phrContainer
                phrContainer.appendChild(idContainer);
                phrContainer.appendChild(buttonsDiv);


                // Create container for Name and Status
                let infoContainer = document.createElement('div');
                infoContainer.className = 'info-container';

                let nameLabel = document.createElement('span');
                nameLabel.style.cssText = labelStyle;
                nameLabel.innerText = 'Name:';
                let nameValue = document.createElement('span');
                nameValue.style.cssText = valueStyle; 
                nameValue.innerText = patient.name;

                let statusLabel = document.createElement('span');
                statusLabel.style.cssText = labelStyle;
                statusLabel.innerText = 'Status:';
                let statusValue = document.createElement('span');
                statusValue.style.cssText = valueStyle; 
                statusValue.innerText = phrId.status;

                // Append Name and Status and Id to the id/infoContainer
                idContainer.appendChild(nameLabel);
                idContainer.appendChild(nameValue);
                idContainer.appendChild(statusLabel);
                idContainer.appendChild(statusValue);
                infoContainer.appendChild(idLabel);
                infoContainer.appendChild(idValue);

                // Append phrContainer and infoContainer to the list item
                li.appendChild(phrContainer);
                li.appendChild(infoContainer);

                // Append the list item to the container element (phrList in this case)
                phrList.appendChild(li);
            }
        }

        console.log(response, "patient pendings");

    }
    
    
    async populatePatientsPending(provider){
        console.log(provider,"patients provider")
        var listGroup = document.getElementById('desktopListGroupPatients');
        const identity = await this.client.getIdentity();
        const self = this; 
        let counter = 0;
        if(provider.pendingPatients) {
            for (const patient of provider.pendingPatients) {
                let patientName;
                try {
                    var listItem = document.createElement('li');
                    listItem.className = 'list-group-item d-flex justify-content-between align-items-center';
                
                    // add await before this.client.getPatient(patient)
                    patientName = await this.client.getPatient(patient);
                    listItem.id = `patient-${patientName.name}-${counter}`;
                    counter += 1;

                    listItem.innerHTML = `
                        <img class="img-fit" src="https://res.cloudinary.com/demo/image/upload/w_0.7,e_blur:400/front_face.jpg" alt="Patient Image">
                        ${patientName.name}
                        <div>
                        <button class="btn  visit-btn">Visit</button>
                        <button class="btn seen-btn" id="seen-${patientName.name}-${counter}">Seen</button>
                        </div>
                    `;
        
                    listGroup.appendChild(listItem);
        
                    listItem.querySelector('.visit-btn').addEventListener('click', function() {
                        window.open('/visit.html?id=' + patient, '_blank');
                    });
        
                    listItem.querySelector(`#seen-${patientName.name}-${counter}`).addEventListener('click', async (patientName, counter) => {
                        const result = await self.client.removePatient(patient, identity.name); 
                        console.log(result.success, "test");
                        if (result.success === true) {
                            document.getElementById('desktopListGroupPatients').innerHTML = "";
                            console.log("here first");
                            let refreshedProvider = await this.client.getProvider(identity.name);
                            await this.populatePatientsPending(refreshedProvider);
                        }
                    });
        
                } catch (err) {
                    console.log(err);  
                }
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
                console.log(patients, "get all patients");
        
                // Get the identity
                const identity = await this.client.getIdentity();
        
                // Get a reference to the list element
                const list = document.getElementById('allPatientsList');
        
                // Iterate over the array of patients
                for (let patient of patients.patients) {
                    console.log(patient,"loop");
        
                    // Create a new list item for each patient
                    let li = document.createElement('li');
                    li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'text-center','align-items-center');
                    
                    // Create text node for patient name and age
                    let textNode = document.createTextNode(`Name: ${patient.name}, Age: ${patient.age}`);
                    li.appendChild(textNode);
                    
                    // Create the button container
                    let buttonContainer = document.createElement('div');
        
                    // Create the Add button
                    let addButton = document.createElement('button');
                    addButton.classList.add('btn', 'seen-btn');
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
                    editButton.classList.add('btn', 'seen-btn');
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
        const fromDateInput = document.getElementById('fromDate').value;
        const toDateInput = document.getElementById('toDate').value;
        const patientIdInput = document.getElementById('patientId').value.trim();  // New patient ID input

        let phrResultsArray = [];
    
        // Get a reference to the accordion and clear any existing results
        const accordion = document.getElementById('phrAccordion');
        accordion.innerHTML = "";
    
        // Decide which function to call based on inputs provided
        let temp;
        if (patientIdInput !== "" && phrIdInput === "" && fromDateInput === "" && toDateInput === "") {
            // If only patientIdInput is provided, call getPHR with patientIdInput
            temp = await this.client.getAllPHR(patientIdInput);
        } else if (phrIdInput !== "") {
            temp = await this.client.getPHR(phrIdInput);
        } else if (fromDateInput !== "" && toDateInput !== "" && patientIdInput !== "") {
            // For date or date range search, patientIdInput must be provided
            temp = await this.client.getPHRDateRange(patientIdInput, fromDateInput, toDateInput);
        } else if (fromDateInput !== "" && toDateInput !== "" && patientIdInput !== "") {
            // For date or date range search, patientIdInput must be provided
            temp = await this.client.getPHRDateRange(patientIdInput, fromDateInput, toDateInput);
        } 
        if (temp == undefined) { 
            let toastHTMLElement = document.getElementById('YodaSearchBad');
            let toastElement = new bootstrap.Toast(toastHTMLElement);
            toastElement.style = "text-center"
            toastElement.show();
        }
        // Check if the result is an array. If so, assign it directly to phrResultsArray. If not, wrap it in an array.
        phrResultsArray = temp && Array.isArray(temp.phrId) ? temp.phrId :  temp.phrId = [temp];

        console.log(phrResultsArray.reverse(), "after check for array")
        let toastHTMLElement = document.getElementById('YodaSearch');
        let toastElement = new bootstrap.Toast(toastHTMLElement);
        toastElement.style = "text-center"
        toastElement.show();
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
                accordionButton.classList.add("text-center","d-block","py-3");
                // !Change This
                accordionButton.innerText = `Date ${phr.date}`;
                const iconSearch = document.createElement('img');
                iconSearch.classList.add('iconography');
                iconSearch.src = '/images/Icons/PHR.png';
                const statusDiv = document.createElement('div');
                const statusHead = document.createElement("h4");
                statusHead.innerText += `Status: ${phr.status}`
                accordionButton.appendChild(iconSearch);
                statusDiv.appendChild(statusHead);
                accordionButton.appendChild(statusHead);
    
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
            let toastHTMLElement = document.getElementById('YodaSearchBad');
            let toastElement = new bootstrap.Toast(toastHTMLElement);
            toastElement.style = "text-center"
            toastElement.show();
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
