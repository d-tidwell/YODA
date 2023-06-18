import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

/**
 * Client to call the Yoda Service.

  */
export default class YodaClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['editCompForm','parseComp', 'clientLoaded', 'getIdentity', 'login', 'logout', 'getProvider', 'getPatient', 'createPatient', 'removePatient', 'getAllPHRByProvider', 'getAllPatients'];
        this.bindClassMethods(methodsToBind, this);

        this.authenticator = new Authenticator();;
        this.props = props;

        axios.defaults.baseURL = process.env.API_BASE_URL;
        this.axiosClient = axios;
        this.clientLoaded();
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     */
    clientLoaded() {
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady(this);
        }
    }

    /**
     * Get the identity of the current user
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The user information for the current user.
     */
    async getIdentity(errorCallback) {
        try {
            const isLoggedIn = await this.authenticator.isUserLoggedIn();

            if (!isLoggedIn) {
                return undefined;
            }

            return await this.authenticator.getCurrentUserInfo();
        } catch (error) {
            console.log(error);
            this.handleError(error, errorCallback)
        }
    }

    async login() {
        this.authenticator.login();
    }

    async logout() {
        await this.authenticator.logout();
        window.location = 'index.html';
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }

    async isLoggedIn() {
        return this.authenticator.isUserLoggedIn();
    }

    /**
    * Gets the profile for the given ID.
    * @param id Unique identifier for a profile
    * @param errorCallback (Optional) A function to execute if the call fails.
    * @returns The profile's metadata.
    */

    async getProvider(providerName, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view pending.");
            const response = await this.axiosClient.get(`/provider/${providerName}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            console.error(error); // or do something with the error
            if (errorCallback) {
                errorCallback(error);
            }
            throw error; // rethrow the error
        }
    }


    async createProvider(providerName, providerEmail, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view pending.");
            const response = await this.axiosClient.post(`/provider/create/${providerName}/${providerEmail}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            console.error(error, "ORIGINAL ERROR");
            this.handleError(error);
            return undefined;
        }
    }

    async getPatient(patientId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view patients.");
            const response = await this.axiosClient.get(`/patient/${patientId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async getAllPatients(errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view patients.");
            const response = await this.axiosClient.get(`/patient/all/anyone`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async createPatient(name, age, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can create patients.");
            const response = await this.axiosClient.post(`/patient/new`, {
                patientName: name,
                patientAge: age
            }, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async createPHR(patientId, providerName, date, type, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can create patients.");
            const response = await this.axiosClient.post(`/patient/phr/${patientId}`, {
                providerName: providerName,
                date: date,
                type: type
            }, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback)
            return undefined;
        }
    }

    async addPatientToProvider(patientId, providerName, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view patients.");
            const response = await this.axiosClient.post(`/provider/${providerName}/${patientId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async removePatient(patientId, providerName, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view patients.");
            const response = await this.axiosClient.put(`/provider/remove/${patientId}/${providerName}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            console.log(response.data, "remove");
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async getPHR(phrId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view patients.");
            const response = await this.axiosClient.get(`/patient/phr/single/${phrId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            console.log("phrID search", response)
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async getPHRDate(patientId, date, errorCallback) {
        console.log("hit date");
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view patients.");
            const response = await this.axiosClient.get(`/patient/byDateRange/${patientId}`, {
                params: {
                    from: date,
                    to: date
                },
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback)
            return undefined;
        }
    }

    async getPHRDateRange(patientId, from, to, errorCallback) {
        console.log("hit range");
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view patients.");
            const response = await this.axiosClient.get(`/patient/byDateRange/${patientId}`, {
                params: {
                    from: from,
                    to: to
                },
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
            return undefined;
        }
    }

    async getAllPHRByProvider(providerName, errorCallback) {
        console.log("get all by provider");
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view patients.");
            const response = await this.axiosClient.get(`/phr/byProviderId/${providerName}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async getAllPHR(patientId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view patients.");
            const response = await this.axiosClient.get(`/patient/byId/${patientId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
            return undefined;
        }
    }

    async getPresignedS3(filename, phrId, date, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view patients.");
            const response = await this.axiosClient.get(`/dictation/audio/${filename}`, {
                params: {
                    phrId: phrId,
                    date: date
                },
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }

    async dropInTheBucket(presignedS3Url, audioBlob, errorCallback) {
        try {
            const response = await this.axiosClient.put(presignedS3Url, audioBlob, {
                headers: {
                    'Content-Type': 'audio/webm',
                }
            });
            return response;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }

    async updateDictation(PhrId, PhrDate, fileName, type, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view patients.");
            const response = await this.axiosClient.put(`/dictate/${PhrId}/${PhrDate}/${fileName}/${type}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            console.log(response);
            return response;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }
    async iconoclass(typeHead) {
        let medicationImage = "/images/Icons/pillhand.png";
        let anatomyImage = "/images/Icons/kidney.png";
        let medicalConditionImage = "/images/Icons/hospital.png";
        let timeExpressionImage = "/images/Icons/handshield.png";
        let behavioralImage = "/images/Icons/kidney.png";
        let protectedHealthImage = "/images/Icons/suitcase.png";
        let testTreatmentImage = "/images/Icons/testsh.png";

        switch (typeHead) {
            case 'ANATOMY':
                return anatomyImage;
            case 'BEHAVIORAL_ENVIRONMENTAL_SOCIAL':
                return behavioralImage;
            case 'MEDICAL_CONDITION':
                return medicalConditionImage;
            case 'MEDICATION':
                return medicationImage;
            case 'PROTECTED_HEALTH_INFORMATION':
                return protectedHealthImage;
            case 'TEST_TREATMENT_PROCEDURE':
                return testTreatmentImage;
            case 'TIME_EXPRESSION':
                return timeExpressionImage;
            default:
                return '/images/Icons/doc.png';
        }
    }

    async parseComp(compData) {
        let containerKeys = document.createElement("div");
        containerKeys.className = "containerKeys";

        if (compData != null) {
            const hashMap = { key: compData };
            const jsonString = hashMap.key;
            let jsonObject;

            try {
                jsonObject = JSON.parse(jsonString);
            } catch (error) {
                console.error('Invalid JSON string:', error);
                return containerKeys;
            }

            for (const parentKey in jsonObject) {
                if (jsonObject.hasOwnProperty(parentKey)) {
                    const boxLabels = document.createElement("div");
                    boxLabels.className = "headingBoxPHR"
                    const parentHeading = document.createElement("h5");
                    parentHeading.innerHTML = parentKey;
                    parentHeading.style = "color: white;"
                    const icon = document.createElement("img");
                    icon.style = "iconography-phr-result";
                    icon.src = await this.iconoclass(parentKey.toString());
                    icon.className = "iconography-phr-result";
                    boxLabels.appendChild(icon);
                    boxLabels.appendChild(parentHeading);
                    containerKeys.appendChild(boxLabels);

                    const rents = jsonObject[parentKey];

                    for (const children in rents) {
                        const childHeading = document.createElement("h6");
                        childHeading.innerHTML = children;
                        const seperator = document.createElement("div");
                        childHeading.className = "subHeadingBox"
                        seperator.appendChild(childHeading);
                        containerKeys.appendChild(seperator);

                        for (const sibling in jsonObject[parentKey][children]) {
                            const siblingText = document.createElement("p");
                            siblingText.style = "font-weight: bold";
                            seperator.appendChild(siblingText);
                            siblingText.innerHTML = sibling;
                            siblingText.textContent = await this.capitalizeFirstLetter(sibling);
                            const cuz = jsonObject[parentKey][children][sibling];
                            const sepSibs = document.createElement("div");
                            const sepSibsTakes = document.createElement("div");

                            for (const key in cuz) {
                                if (cuz.hasOwnProperty(key) && Object.keys(cuz[key]).length !== 0) {
                                    const subObject = cuz[key];

                                    const subObjectElement = document.createElement("p");
                                    const subObjectElementBreak = document.createElement("hr");
                                    subObjectElementBreak.classList.add("breaker-point");
                                    let subObjectContent = '';

                                    for (const subKey in subObject) {
                                        if (subObject.hasOwnProperty(subKey)) {
                                            const subValue = subObject[subKey];
                                            subObjectContent += `${subKey}: ${JSON.stringify(subValue)} `;
                                        }
                                    }

                                    subObjectElement.textContent = `${subObjectContent}`;

                                    sepSibsTakes.appendChild(subObjectElement);
                                    sepSibsTakes.appendChild(subObjectElementBreak);

                                }
                            }

                            if (sepSibsTakes.childElementCount > 0) {
                                sepSibs.appendChild(siblingText);
                                sepSibs.appendChild(sepSibsTakes);
                                containerKeys.appendChild(sepSibs);
                            }
                        }
                    };
                }
            }
        }
        console.log(containerKeys)
        return containerKeys;
    }
    async editCompForm(compData) {
        let containerKeys = document.createElement("div");
        containerKeys.className = "containerKeys";

        if (compData != null) {
            const hashMap = { key: compData };
            const jsonString = hashMap.key;
            let jsonObject;

            try {
                jsonObject = JSON.parse(jsonString);
            } catch (error) {
                console.error('Invalid JSON string:', error);
                return containerKeys;
            }

            for (const parentKey in jsonObject) {
                if (jsonObject.hasOwnProperty(parentKey)) {
                    const boxLabels = document.createElement("div");
                    boxLabels.className = "headingBoxPHR"
                    const parentHeading = document.createElement("h5");
                    parentHeading.innerHTML = parentKey;
                    parentHeading.style = "color: white;"
                    const icon = document.createElement("img");
                    icon.style = "iconography-phr-result";
                    icon.src = await this.iconoclass(parentKey.toString());
                    icon.className = "iconography-phr-result";
                    boxLabels.appendChild(icon);
                    boxLabels.appendChild(parentHeading);
                    containerKeys.appendChild(boxLabels);

                    const rents = jsonObject[parentKey];

                    for (const children in rents) {
                        const childHeading = document.createElement("h6");
                        childHeading.innerHTML = children;
                        const seperator = document.createElement("div");
                        childHeading.className = "subHeadingBox"
                        seperator.appendChild(childHeading);
                        containerKeys.appendChild(seperator);

                        for (const sibling in jsonObject[parentKey][children]) {
                            const siblingText = document.createElement("p");
                            siblingText.style = "font-weight: bold";
                            seperator.appendChild(siblingText);
                            siblingText.innerHTML = sibling;
                            siblingText.textContent = await this.capitalizeFirstLetter(sibling);
                            const cuz = jsonObject[parentKey][children][sibling];
                            const sepSibs = document.createElement("div");
                            const sepSibsTakes = document.createElement("div");

                            for (const key in cuz) {
                                if (cuz.hasOwnProperty(key) && Object.keys(cuz[key]).length !== 0) {
                                    const subObject = cuz[key];

                                    const subObjectElement = document.createElement("p");
                                    const subObjectElementBreak = document.createElement("hr");
                                    subObjectElementBreak.classList.add("breaker-point");
                                    let subObjectContent = '';

                                    for (const subKey in subObject) {
                                        if (subObject.hasOwnProperty(subKey)) {
                                            const subValue = subObject[subKey];
                                            subObjectContent += `${subKey}: ${JSON.stringify(subValue)} `;
                                        }
                                    }

                                    subObjectElement.textContent = `${subObjectContent}`;

                                    sepSibsTakes.appendChild(subObjectElement);
                                    sepSibsTakes.appendChild(subObjectElementBreak);

                                }
                            }

                            if (sepSibsTakes.childElementCount > 0) {
                                sepSibs.appendChild(siblingText);
                                sepSibs.appendChild(sepSibsTakes);
                                containerKeys.appendChild(sepSibs);
                            }
                        }
                    };
                }
            }
        }
        console.log(containerKeys)
        return containerKeys;
    }
    async capitalizeFirstLetter(str) {
        return await str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(error, errorCallback) {
        console.error(error);

        const errorFromApi = error?.response?.data?.error_message;
        if (errorFromApi) {
            console.error(errorFromApi)
            error.message = errorFromApi;
        }

        if (errorCallback) {
            errorCallback(error);
        }
    }
}
