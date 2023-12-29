# Use and Maintenance
We have tested our app on multiple different operating systems and web browsers.
To name a few, we tested on Windows, MacOS, and multiple Linux distributions such as Ubuntu, Fedora, Arch Linux, Linux Mint for operating systems, and Chrome, Opera, Brave for web browsers.
On the mobile side, we used multiple different devices from different vendors to test all releases of our application.

## Software

### Application links

- Frontend: http://ec2-51-20-78-40.eu-north-1.compute.amazonaws.com/home
- Backend: http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/swagger-ui/index.html#/
- Annotation: http://ec2-3-72-52-0.eu-central-1.compute.amazonaws.com/swagger-ui/index.html#/

### Building the docker image
There will be three different env files for the project. They should be first placed on the corresponding folders:

- Backend -> app/backend/src/main/resources/.env
- Frontend -> app/frontend/.env
- Annotation Backend -> app/annotation/annotation/src/main/resources/.env

After all the environments are placed in the correct folders, the application can be built by running the following command in app directory:

    docker compose build

And can be run with the following command:

    docker compose up

After the containers are started, the frontend will run on port 80, application backend will run on port 3001 and annotation backend will run on port 3002. If for any reason, these ports are not available in your system, you should change them from the compose.yml in the app folder.

After we have done with our application, we can stop it by running the following command in the same directory:

     docker compose down

Disclaimer: Running the app on local machine will have few limitations. Firstly, since all images are stored in our AWS server, there won't be any images to show on local, only the images that are uploaded from the backend that run in your local will show up (you should create some posts/games etc.). Secondly since the url will be different from our frontend, the annotations from our frontend can not be seen, since the source field will not match. Instead you can add your own annotations and see them.
