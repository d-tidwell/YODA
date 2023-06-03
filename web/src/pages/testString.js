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

        this.bindClassMethods(['mount', 'search', 'displaySearchResults','populatePatientsPending','clientLoaded'], this);

        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
        this.dataStore.addChangeListener(this.displaySearchResults);
    }
    async clientLoaded(){
        const identity =  await this.client.getIdentity();
        const provider =  await this.client.getProvider(identity.name);
        this.populatePatientsPending(provider);
    }
    /**
     * Add the header to the page and load the Client.
     */
    mount() {

        this.header.addHeaderToPage();

        this.client = new yodaClient();

        this.displaySearchResults();
       
        this.clientLoaded();
    }

    /**
     * Uses the client to perform the search, 
     * then updates the datastore with the criteria and results.
     * @param evt The "event" object representing the user-initiated event that triggered this method.
     */
    async search(evt) {
        // Prevent submitting the from from reloading the page.
        evt.preventDefault();

        const searchCriteria = document.getElementById('search-criteria').value;
        const previousSearchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);

        // If the user didn't change the search criteria, do nothing
        if (previousSearchCriteria === searchCriteria) {
            return;
        }

        if (searchCriteria) {
            const results = await this.client.search(searchCriteria);

            this.dataStore.setState({
                [SEARCH_CRITERIA_KEY]: searchCriteria,
                [SEARCH_RESULTS_KEY]: results,
            });
        } else {
            this.dataStore.setState(EMPTY_DATASTORE_STATE);
        }
    }

    /**
     * Pulls search results from the datastore and displays them on the html page.
     */
    displaySearchResults() {

    }
    async populatePhrPending(){
        
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
    
    

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const testString = new TestString();
    testString.mount();
};

window.addEventListener('DOMContentLoaded', main);
