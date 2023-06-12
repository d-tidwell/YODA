import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
  */
export default class YodaClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['parseComp','clientLoaded', 'getIdentity', 'login', 'logout', 'getProvider','getPatient', 'createPatient','removePatient','getAllPHRByProvider','getAllPatients'];
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

    async isLoggedIn(){
        return this.authenticator.isUserLoggedIn();
    }

     /**
     * Gets the profile for the given ID.
     * @param id Unique identifier for a profile
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The profile's metadata.
     */

    async getProvider(providerName, errorCallback){
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
            this.handleError(error, errorCallback)
        }
    }

    async getPatient(patientId, errorCallback){
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

    async getAllPatients(errorCallback){
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

    async createPatient(name, age, errorCallback){
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

    async createPHR(patientId, providerName, date, type, errorCallback){
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
        }
    }
    
    async addPatientToProvider(patientId, providerName, errorCallback){
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

    async removePatient(patientId, providerName, errorCallback){
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

    async getPHR(phrId, errorCallback){
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

    async getPHRDate(patientId, date, errorCallback){
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
        }
    }

    async getPHRDateRange(patientId, from, to, errorCallback){
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
            this.handleError(error, errorCallback)
        }
    }
    
    async getAllPHRByProvider(providerName, errorCallback){
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

    async getAllPHR(patientId, errorCallback){
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
            this.handleError(error, errorCallback)
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

    async updateDictation(PhrId, PhrDate, fileName, type, errorCallback){
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
    
    async parseComp(compData) {
        let containerKeys = document.createElement("div");
      
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
                const parentHeading = document.createElement("h5");
                parentHeading.innerHTML = parentKey;
                containerKeys.appendChild(parentHeading);
        
                const children = jsonObject[parentKey];
                
                for(const children in jsonObject[parentKey]) {
                    //console.log(JSON.stringify(children));
                    const childHeading = document.createElement("h7");
                    childHeading.innerHTML = children;
                    const seperator = document.createElement("div");
                    seperator.appendChild(childHeading);
                    containerKeys.appendChild(seperator);

                    for(const sibling in jsonObject[parentKey][children]) {
                        const siblingText = document.createElement("text");
                        siblingText.innerHTML = sibling;
                        const sepSibs = document.createElement("div");
                        sepSibs.appendChild(siblingText);
                        containerKeys.appendChild(sepSibs);
                        
                    }
                };
            }
          }
        }
        return containerKeys;
      }
      
//    /**
//     * Gets the playlist for the given ID.
//     * @param id Unique identifier for a playlist
//     * @param errorCallback (Optional) A function to execute if the call fails.
//     * @returns The playlist's metadata.
//     */
//    async getPlaylist(id, errorCallback) {
//        try {
//            const response = await this.axiosClient.get(`playlists/${id}`);
//            return response.data.playlist;
//        } catch (error) {
//            this.handleError(error, errorCallback)
//        }
//    }
//
//    /**
//     * Get the songs on a given playlist by the playlist's identifier.
//     * @param id Unique identifier for a playlist
//     * @param errorCallback (Optional) A function to execute if the call fails.
//     * @returns The list of songs on a playlist.
//     */
//    async getPlaylistSongs(id, errorCallback) {
//        try {
//            const response = await this.axiosClient.get(`playlists/${id}/songs`);
//            return response.data.songList;
//        } catch (error) {
//            this.handleError(error, errorCallback)
//        }
//    }
//
//    /**
//     * Create a new playlist owned by the current user.
//     * @param name The name of the playlist to create.
//     * @param tags Metadata tags to associate with a playlist.
//     * @param errorCallback (Optional) A function to execute if the call fails.
//     * @returns The playlist that has been created.
//     */
//    async createPlaylist(name, tags, errorCallback) {
//        try {
//            const token = await this.getTokenOrThrow("Only authenticated users can create playlists.");
//            const response = await this.axiosClient.post(`playlists`, {
//                name: name,
//                tags: tags
//            }, {
//                headers: {
//                    Authorization: `Bearer ${token}`
//                }
//            });
//            return response.data.playlist;
//        } catch (error) {
//            this.handleError(error, errorCallback)
//        }
//    }
//
//    /**
//     * Add a song to a playlist.
//     * @param id The id of the playlist to add a song to.
//     * @param asin The asin that uniquely identifies the album.
//     * @param trackNumber The track number of the song on the album.
//     * @returns The list of songs on a playlist.
//     */
//    async addSongToPlaylist(id, asin, trackNumber, errorCallback) {
//        try {
//            const token = await this.getTokenOrThrow("Only authenticated users can add a song to a playlist.");
//            const response = await this.axiosClient.post(`playlists/${id}/songs`, {
//                id: id,
//                asin: asin,
//                trackNumber: trackNumber
//            }, {
//                headers: {
//                    Authorization: `Bearer ${token}`
//                }
//            });
//            return response.data.songList;
//        } catch (error) {
//            this.handleError(error, errorCallback)
//        }
//    }
//
//    /**
//     * Search for a song.
//     * @param criteria A string containing search criteria to pass to the API.
//     * @returns The playlists that match the search criteria.
//     */
//    async search(criteria, errorCallback) {
//        try {
//            const queryParams = new URLSearchParams({ q: criteria })
//            const queryString = queryParams.toString();
//
//            const response = await this.axiosClient.get(`playlists/search?${queryString}`);
//
//            return response.data.playlists;
//        } catch (error) {
//            this.handleError(error, errorCallback)
//        }
//
//    }

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
