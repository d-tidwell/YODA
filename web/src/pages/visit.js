import yodaClient from '../api/yodaClient';
import BindingClass from "../util/bindingClass";
import Header from '../components/header';
import DataStore from "../util/DataStore";

class Visit extends BindingClass {
    constructor() {
      super();
      this.bindClassMethods(['clientLoaded', 'mount', 'submitForm', 'uploadAudioToS3'], this);
      this.dataStore = new DataStore();
      this.header = new Header(this.dataStore);
      this.client = new yodaClient();
      this.clientLoaded();
    }
  
    async clientLoaded() {
      // Initialize audio recording variables
      this.mediaRecorder = null;
      this.chunks = [];
      this.recordedAudio = null;
  
      // Get reference to record, stop, play buttons, and audio element
      this.recordButton = document.getElementById("recordVisit");
      this.stopButton = document.getElementById("stopVisit");
      this.playButton = document.getElementById("playAudio");
      this.audioElement = document.getElementById("audio_preview");
      this.submitButton = document.getElementById("submitVisit");
  
      // Attach event listeners
      this.recordButton.addEventListener("click", this.startRecording);
      this.stopButton.addEventListener("click", this.stopRecording);
      this.playButton.addEventListener("click", this.playAudio);
      this.submitButton.addEventListener("click", this.submitForm);
  
    }
  
    async mount() {
      this.header.addHeaderToPage();
      const loggedIn = await this.client.isLoggedIn();
    }
    
    async submitForm(){
        //const s3string = this.client.getS3Presigned
        this.uploadAudioToS3(s3string);
        //make a call to create the dictation object & populate the data

    }

    async uploadAudioToS3(presignedS3Url) {
        if (!this.recordedAudio) {
          console.warn("No recorded audio available.");
          return;
        }
        
        // Convert recorded audio to blob
        const response = await fetch(this.recordedAudio.src);
        const audioBlob = await response.blob();
      
        // Upload blob to S3
        const uploadResponse = await fetch(presignedS3Url, {
          method: 'PUT',
          body: audioBlob,
          headers: {
            'Content-Type': 'audio/ogg',
          }
        });
      
        if (!uploadResponse.ok) {
          throw new Error(`Failed to upload audio to S3: ${uploadResponse.statusText}`);
        }
        
        console.log('Audio successfully uploaded to S3.');
      }
      
    startRecording = () => {
      navigator.mediaDevices.getUserMedia({ video: false, audio: true })
        .then((stream) => {
          this.audioElement.srcObject = stream;
          if (MediaRecorder.isTypeSupported('audio/mpeg')) {
            this.mediaRecorder = new MediaRecorder(stream, { mimeType: 'audio/ogg; codecs=opus' });
          } else {
            // Fall back to default format if MPEG is not supported
            this.mediaRecorder = new MediaRecorder(stream);
            console.log('audio/ogg; codecs=opus not supported');
          }
          this.mediaRecorder.start();
          this.mediaRecorder.ondataavailable = this.onDataAvailable;
          this.recordButton.disabled = true;
          this.stopButton.disabled = false;
          this.playButton.disabled = true;
          this.recordedAudio = null;
        })
        .catch(this.onRecordFail.bind(this));
    }
  
    stopRecording = () => {

        this.mediaRecorder.stop();
        this.mediaRecorder = null;
        this.recordButton.disabled = false;
        this.stopButton.disabled = true;
        this.playButton.disabled = false;
    
        setTimeout(() => {
        const audioBlob = new Blob(this.chunks, { type: "audio/mpeg" });
        const audioURL = window.URL.createObjectURL(audioBlob);
        this.recordedAudio = new Audio(audioURL);
        this.chunks = [];
        }, 100);
    };
      
  
    playAudio = () => {
      if (this.recordedAudio) {
        this.recordedAudio.play();
      } else {
        console.warn("No recorded audio available.");
      }
    }
  
    // Handle available audio data
    onDataAvailable = (event) => {
      this.chunks.push(event.data);
    }
  
    // Handle recording failure
    onRecordFail(e) {
      console.log(e);
    }
  }
  
  /**
   * Main method to run when the page contents have loaded.
   */
  const main = async () => {
    const visit = new Visit();
    visit.mount();
  };
  
  window.addEventListener('DOMContentLoaded', main);
  
