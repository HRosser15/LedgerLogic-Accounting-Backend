# Back-End
This repository will host the Back-End of our Ledger Logic Software

## Prerequisites
- Java JDK 11 or higher
- Node.js 10 or higher
- IntelliJ IDEA Ultimate (for students)

# Getting Set Up

## Installing Node.js and npm on Windows

### Download the Installer:
- Go to the [Node.js website](https://nodejs.org/).
- You will see two versions available for download: LTS (Long Term Support) and Current. For most users, the LTS version is recommended as it is more stable.
- Click on the Windows Installer (.msi) to download the installer suitable for your system architecture (32-bit or 64-bit).

### Run the Installer:
- Once the installer is downloaded, run it to start the setup process.
- Click through the installer — it will ask you to agree to the license agreement, choose the install location, and select which components to include. The default settings are usually sufficient.
- Ensure that the checkboxes to automatically install necessary tools and "Add to PATH" are selected. This step is crucial as it allows you to run Node.js and npm from the command line.

### Verify the Installation:
- Open a command prompt (you can search for `cmd` in the Start menu).
- Type `node -v` and press Enter. You should see the Node.js version printed, indicating that Node.js is installed.
- Type `npm -v` to check if npm (Node Package Manager) is installed along with Node.js and to see its installed version.

### Update npm (Optional):
- Although npm gets installed with Node.js, it might not always be the latest version. To update npm to its latest version, you can run the following command in your command prompt:
```bash
npm install npm@latest -g
```

### Note for macOS and Linux Users
#### For developers using macOS:
- You can install Node.js and npm using Homebrew (a package manager for macOS). Just run:
```bash
brew install node
```

#### For developers using Linux:
- It’s often best to install Node.js via a package manager specific to your Linux distribution. Instructions for these can be found on the Node.js website under the Linux installations tab, or you can use Node Version Manager (nvm) for more flexibility in managing multiple versions of Node.js.


## Install IntelliJ ultimate if you don’t already have it.
I don’t think Community Edition comes with Spring Boot.
IntelliJ IDEA Ultimate is free for students.
https://www.jetbrains.com/idea/download/?section=windows

### Clone the Repository with intelliJ
- Go to the Welcome screen for IntelliJ IDEA
- Click ```Get from VCS```
- Paste ```https://github.com/Ledger-Logic/Back-End.git``` into the URL box
- Select the desired directory for your local repo
- Click the ```Clone``` button

#### “Clone failed … repository not found”
If this error message popped, up follow these steps:
- Open or create a new blank project (you can delete this after)
- Click **File >> Settings >> Version Control >> Git** and check the “**User credential helper**” box (uncheck it if it is checked already).
- Click ```Apply``` then ```OK```
- Click **File >> Close Project** and repeat the steps for cloning the repo to IntelliJ

### Download the JDK
You may need to install the JDK if you don’t have it installed already.
IntelliJ should pop up a yellow box along the top of the window suggesting you install it.
Click ```Download Oracle OpenJDK 21.0.2```

### Checkout the appropriate branch
By default you should be in ```main```, if this isn’t your desired branch:
- Click the branch button at the top next to the project name.
- Click the desired branch then click checkout

### Ensure your branch is up to date
Once you’re in the desired branch: 
- Click the branch button at the top left
- Click ```Fetch``` (dashed arrow at very top)
- Review any changes
- Click the branch button at the top left
- Click ```Update Project```

## Setting Up Email Server
### Installation
#### Install npm

```bash
npm install -g maildev
```
the email server interface can be accessed at ```http://0.0.0.0:1080```
### Configuration
Replace your application.properties/.yml file with the following: 
```
spring.profiles.active=development

spring.mail.host=localhost
spring.mail.port=1025
spring.mail.properties.mail.smtp.auth=false
spring.mail.properties.mail.smpt.starttls.enable=false
spring.mail.properties.mail.smpt.starttls.required=false

spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```
(NOTE: you will NOT be replacing the content of your ```application-development.properties``` or ```application-production.properties```)

### Running the Email Server
- Open a command prompt
- Run ```maildev```
- The application should now work without issue.

### Run the Server
You should see ```LedgerLogicApplication``` at the top of the IntelliJ window. If you do not, navigate to the LegerLogicApplication. This is the file we use to boot the server.
**LodgerLogic >> src >> main >> java >> com.ledgerlogic >> LedgerLogicApplication**
- Double click **LedgerLogicApplication** to open the file
- Click the green ‘Run’ arrow at the top

## Accessing the h2 database
### Signing into the h2 console
- Navigate to http://localhost:8080/h2-console in your browser
- make sure the **JDBC URL** is set to ```jdbc:h2:file:./h2db```
- user: ```admin```
- password: ```password```

### You’re all set up
Now you just use SQL queries in the h2 database.
- To test if the application is working as expected, click the ACCOUNTS table. This should generate an SQL query to view all fields in the ACCOUNTS table. Click 'run', and if the application is working properly, you should see several accounts prepopulated into the table.

## Finding the right URL / Endpoint for your requests on the front-end
#### Controller Classes
Looking at the controllers in the backend, you will find each class has an annotation with the base URL.
For instance, in the UserController it is **”/users”**
```bash
@RequestMapping("/users")
public class UserController {
```
#### Controller methods
Each method inside the controller class will have URL templates that define the structure that the method can handle. 
For instance, in the UserController class, the URL template for getAllUsers is **”/allUsers”**
```bash
@GetMapping("/allUsers")
public List<User> getAllUsers(){
```
#### Writing HTTP requests to specific URLS / Endpoints
Each request will follow the format:
```bash
return axios.<request_type>(`http://localhost:8080/<base_URL>/<URL_template>`);
```
Where ```<request_type>``` can be replaced with:
- get
- post
- put
- delete
You can use this to create service functions in the frontend such as one that will fetch all users for the admin.
```bash
export const fetchUsers = () => {
    return axios.get(`http://localhost:8080/users/allUsers`);
};
```

### Checking Emails
- Open a web browser
- In the URL bar, enter ```http://localhost:1080/```
- You should now see any emails that get sent
#### Viewing Attachments in emails
- Open the email you would like to view the attachment for.
- At the top of the window, click the "Text" button and select "Attachments". This will display a download for the attachment.


## Troubleshooting

- **Issue**: IntelliJ does not recognize Spring Boot configurations.
  **Solution**: Ensure you have the Spring Boot plugin enabled in IntelliJ. Go to `File > Settings > Plugins` and search for Spring Boot.

- **Issue**: JDK installation prompts do not appear.
  **Solution**: Manually download the JDK from [Oracle's website](https://www.oracle.com/java/technologies/javase-downloads.html) and configure the JDK in IntelliJ by going to `File > Project Structure > Project` and setting the Project SDK to the installed JDK.

- **Issue**: Maildev server doesn't start.
  **Solution**: Ensure Node.js is installed correctly and the PATH environment variable includes Node.js. Try reinstalling Maildev if issues persist.


