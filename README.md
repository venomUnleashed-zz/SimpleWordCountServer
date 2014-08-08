SimpleWordCountServer
=====================

HttpServer based java server which indexes text documents and returns word count using lucene


This is simple java server which uses lucene to index text files and and provide frequencies of words in them.

example:

$curl -i http://localhost:10000/?query=apple
HTTP/1.1 200 OK
Content-type: application/json
Content-length: 11
Date: Fri, 08 Aug 2014 12:26:25 GMT

{"count":1}

All documents are indexed in-memory everytime the server starts. New documents can also be added by copying them to corpus and calling /reload command.

System Requirements:
-------------------
Java 1.7

Installation:
------
1. Download and extract swcs-latest.tbz from releases.
2. Place your text documents in corpus folder.
3. Run java -jar SimpleWordCountServer.jar {port} . This will start the server on given port. If unspecified the server starts at default port 10000.<br/> 
example: java -jar SimpleWordCountServer.jar 10001 launches server at 100001 port.

Api:
--------
1. /?query= : To get total count of word in existing corpus. A JSON of following format is returned : {count:10}
2. /reload : To re-index all documents in corpus folder. New files can be indexed by adding them to corpus folder and firing this url.


Project Structure:
------------------
Following is package-wise descrion of each class:<br/>
1. com.swcs.server<br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a. Launcher : This class holds the main function. When called an HttpServer is launched at specified or default port.<br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b. Router : This is the router class which passes on url requests to respective handlers. Every handler must register to router by specifying the url path to which they'll cater.<br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;c. GenericRequestHandler : This is an interface. It must be implemented by a Request Handler in order to register itself to the router.<br/>
2. com.swcs.search<br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a. LuceneSearchUtility: This class uses lucene to index text documents in corpus folder. All indexes are maintained in-memory. It also maintains a hashmap with frequencies of all terms.<br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b. SearchHandler: Request handler class which handles / and /reload paths. It uses LuceneSearchUtility to maintain and query indexes.<br/>
3. com.swcs.utils<br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a. Utils : This is a utility class where common functionalities are implemented as static functions.<br/>
