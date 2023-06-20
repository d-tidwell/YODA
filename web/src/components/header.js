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
        imageHeader.classList.add("header-image-quarter", "glowing-image");
        imageHeader.id = "logoId";

        const imageHeader2 = document.createElement('img');
        imageHeader2.src = "images/yoda.png";
        imageHeader2.classList.add("header-image-quarter", "glowing-image");
        imageHeader2.id = "imageYoda";
        imageHeader2.title = "Logout";

        // Add the event listener for logout
        imageHeader2.addEventListener('click', async () => {
            await this.client.logout();
        });

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
        const loginBtn = this.createButton('Login  Dr.', this.client.login);
        loginBtn.classList.add('header-button', 'seen-btn');
        loginBtn.id = "logout-desktop";
        loginBtn.style = "font-family: 'Baloo 2', cursive;";
        loginBtn.addEventListener('click', async () => {
            await this.client.logout();
        });
        return loginBtn;
    }

    createLogoutButton(currentUser) {
        const logoutBtn = this.createButton(`Logout: Dr. ${currentUser.name}`, this.client.logout);
        logoutBtn.id = "logout-desktop";
        logoutBtn.classList.add('header-button', 'seen-btn');
        logoutBtn.addEventListener('click', async () => {
            await this.client.logout();
        });
        return logoutBtn
    }

    createButton(text, clickHandler) {
        const button = document.createElement('button');
        button.classList.add('header-button', 'seen-btn');
        button.type = "button"
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

        month = (month + 1 < 10) ? '0' + (month + 1) : '' + (month + 1);
        day = day < 10 ? '0' + day : day;
        hours = hours < 10 ? '0' + hours : hours;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        seconds = seconds < 10 ? '0' + seconds : seconds;

        return `${year}-${month}-${day}  ${hours}:${minutes}:${seconds}`;
    }

    /**
 * Create a digital military time clock.
 */
    createDigitalClock() {
        const clockDiv = document.createElement('div');
        clockDiv.id = 'digital-clock';
        clockDiv.classList.add('digital-clock');
        clockDiv.innerText = this.getCurrentTime();


        setInterval(() => {
            clockDiv.innerText = this.getCurrentTime();
        }, 1000);  // update every second

        return clockDiv;
    }


}
