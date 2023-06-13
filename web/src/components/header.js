import yodaClient from '../api/yodaClient';
import BindingClass from "../util/bindingClass";

/**
 * The header component for the website.
 */
export default class Header extends BindingClass {
    constructor() {
        super();

        const methodsToBind = [
            'addHeaderToPage', 'createSiteTitle', 'createUserInfoForHeader',
            'createLoginButton', 'createLogoutButton', 'createDigitalClock', 'getCurrentTime'
        ];
        this.bindClassMethods(methodsToBind, this);

        this.client = new yodaClient();
    }


    /**
     * Add the header to the page.
     */
    async addHeaderToPage() {
        const currentUser = await this.client.getIdentity();

        const siteTitle = this.createSiteTitle();
        const userInfo = this.createUserInfoForHeader(currentUser);
  

        const header = document.getElementById('header');

        header.appendChild(siteTitle);
        header.appendChild(userInfo);
        
    }

    createSiteTitle() {
        const imageHeader = document.createElement('img');
        imageHeader.src = "images/logo-no-background.png";
        imageHeader.classList.add("header-image-quarter");
        const imageHeader2 = document.createElement('img');
        imageHeader2.src = "images/yoda.png";
        imageHeader2.classList.add("header-image-quarter");
        const siteTitle = document.createElement('div');
        siteTitle.classList.add('site-title');
        const clock = this.createDigitalClock();
        siteTitle.appendChild(imageHeader);
        siteTitle.appendChild(clock);
        siteTitle.appendChild(imageHeader2);

        return siteTitle;
    }

    createUserInfoForHeader(currentUser) {
        const userInfo = document.createElement('div');
        userInfo.classList.add('user');

        const childContent = currentUser
            ? this.createLogoutButton(currentUser)
            : this.createLoginButton();

        userInfo.appendChild(childContent);

        return userInfo;
    }

    createLoginButton() {
        const loginBtn = this.createButton('Login', this.client.login);
        loginBtn.classList.add('btn');
        loginBtn.style = "font-family: 'Baloo 2', cursive;"
        return loginBtn;
    }

    createLogoutButton(currentUser) {
        const logoutBtn =  this.createButton(`Logout: ${currentUser.name}`, this.client.logout);
        logoutBtn.classList.add('btn')
        logoutBtn.style = "box-shadow: 0 6px 8px rgba(0, 0, 0, 0.2) !important";
        return logoutBtn
    }

    createButton(text, clickHandler) {
        const button = document.createElement('button');
        button.classList.add('header-button');
        button.href = '#';
        button.innerText = text;

        button.addEventListener('click', async () => {
            await clickHandler();
        });

        return button;
    }

    getCurrentTime() {
        const date = new Date();
        let year = date.getFullYear();
        let month = date.getMonth();
        let day = date.getDate();
        let hours = date.getHours();
        let minutes = date.getMinutes();
        let seconds = date.getSeconds();

        month = month < 10 ? '0' + month : month;
        day = day < 10 ? '0' + day : day;
        hours = hours < 10 ? '0' + hours : hours;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        seconds = seconds < 10 ? '0' + seconds : seconds;

        return `${year}-${month}-${day}:${hours}:${minutes}:${seconds}`;
    }

        /**
     * Create a digital military time clock.
     */
    createDigitalClock() {
        const clockDiv = document.createElement('div');
        clockDiv.id = 'digital-clock';
        clockDiv.classList.add('clock');
        clockDiv.classList.add('digital-clock');  
        clockDiv.innerText = this.getCurrentTime();

        setInterval(() => {
            clockDiv.innerText = this.getCurrentTime();
        }, 1000);  // update every second

        return clockDiv;
    }


}
