# AdapterServices
The Adapter Service is a RESTful Web Service. This layer is in charge of comunication with third party services.  
It extracts and gets data like pictures and motivation quotes via REST APIs with [Instagram](https://www.instagram.com/developer/) for pictures and [Forismatic](http://forismatic.com/en/) for motivational quotes.

[API Documentation](http://docs.adapterservices.apiary.io/#)  
[URL Client](https://github.com/introSDE-FinalProject-2016/Telegram-Bot)  
[URL Server (heroku)](https://stark-island-39603.herokuapp.com/sdelab/) 


###Install
In order to execute this server locally you need the following technologies:

* Java (jdk 1.8.0)
* Ant (version 1.9.6)

Then, clone the repository. Run in your terminal:

```
$ git clone like https://github.com/introSDE-FinalProject-2016/AdapterServices.git && cd AdapterServices
```

and run the following command:
```
ant install
```

###Getting Started
To run the server locally then run:
```
ant start
```
