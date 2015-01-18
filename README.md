SlyPanel
========

SlyPanel is an app for Android (4.1+) that takes information from Linux computers/servers and serves as a "Status monitor".

At the moment, external libraries are as follows:

 - JSch (For the SSH backend)
<<<<<<< HEAD
 - HelloCharts (For Graphing the output for CPU usage etc.)
 
 
=======
 - AndroidPlot (For the graphs)
 
Server requirements:

 - Debian based OS (Commands were tested on Ubuntu 14.10)
 - SSH server enabled and port forwarded (If required)
 - Root access to the server (To be comfirmed)
 
The application is in early stages, At the moment it can send SSH commands but not get the response. A testing page is being implemented mainly for debugging and checking commands are being sent properly etc. 

Current status: (Updated 17/01/2015)
  - Application can send SSH commands to any server (Given the credentials and IP are in the code itself)
  - Entire app runs well on Tablets and Phones
  - SSH works fine and has been tested with a DigitalOcean Droplet, Output checking will be set up soon
  - Hoping to add a splash of Material design and maybe some snazzy animations! :D
  - Graphs have finally been added and are working as intended (For now the data is random as a place holder for the SSH response implementation)

Screenshots
===========

![alt tag](http://i.imgur.com/tOztGxV.png)
 - Main server status view - 

![alt tag](http://i.imgur.com/s9t0N2E.png)
 - Connection editor dialog - 


>>>>>>> origin/Alpha-0.6
