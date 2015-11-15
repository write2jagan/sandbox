The goal of this example is the use osgi server as platform to create a simple crud.
To make this example i'll start from 
https://github.com/cmoulliard/Devoxx-2011-HandsOnLab.git and remove all dependency from camel and cxf.
I have rename the package and change completely web module

To run this application I use
->apache-servicemix-4.4.0-SNAPSHOT
->add features:addUrl mvn:org.windsource.osgi.crud.reportincident/features/1.0-SNAPSHOT/xml/features
->features:install reportincident-opejpa


thnks https://github.com/cmoulliard for his work that permit me to improve my competence in osgi

TODO
what i like to do 
The thing I like to do and make it really modular 's application. 
When all the layers are exposed as services, but should be required for each level to make sure to add and remove parts of the application without restarting



