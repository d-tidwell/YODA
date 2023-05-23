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
 * Logic needed for the view playlist page of the website.
 */
class TestString extends BindingClass {
    constructor() {
        super();

        this.bindClassMethods(['mount', 'search', 'displaySearchResults', 'displayProviderDetails'], this);

        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);

    }

    async clientLoaded(){
        const identity = await this.client.getIdentity();
        this.dataStore.set("providerName",identity.name);
    }

    /**
     * Add the header to the page and load the YodaClient.
     */
    mount() {

        this.header.addHeaderToPage();

        this.client = new yodaClient();
        this.clientLoaded();
        this.displaySearchResults();
        this.displayProviderDetails(this.dataStore.get("name"));
       
    }

    /**
     * Remove when done testing!!!!!
     */
   
    displaySearchResults() {
        const tester = this.document.getElementById("testString");
        tester.innerHtml = "test complete";
    }

     /**
     * Pulls search results from the datastore and displays them on the html page.
     */
    displayProviderDetails(providerName){
        const response = this.client.getPatientsPending(providerName);
        const textSpot = document.getElementById("patientsPending");
        textSpot.innerText = response;
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
