# Back-End
This repository will host the Back-End of our Ledger Logic Software

## Getting Set Up
### Install IntelliJ ultimate if you don’t already have it.
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


### Run the Server
Navigate to the LegerLogicApplication. This is the file we use to boot the server.
**LodgerLogic >> src >> main >> java >> com.ledgerlogic >> LedgerLogicApplication**
- Double click **LedgerLogicApplication** to open the file
- Click the green ‘Run’ arrow at the top

### Sign into the h2 database
- Navigate to http://localhost:8080/h2-console in your browser
- make sure the **JDBC URL** is set to ```jdbc:h2:file:./h2db```
- user: ```admin```
- password: ```password```

### You’re all set up
Now you just need to use SQL queries in the h2 database.

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

## Setting Up Email Server
### Installation
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
```
(NOTE: you will NOT be replacing the content of your ```application-development.properties``` or ```application-production.properties```)

### Running the Email Server
- Open a command prompt
- Run ```maildev```
- The application should now work without issue.

### Checking Emails
- Open a web browser
- In the URL bar, enter ```http://localhost:1080/```
- You should now see any emails that get sent




