# boxever-air-shortest-route-finder
Service to find the shortest route from an airport to another given a list of air routes.

## Implementation Approach

### Architecture
The service has been implemented following clean architecture principles as:
- Ports and adapters architecture, having the opportunity of re-implement shell, printer, repository and the proper "finder algorithm".
- Domain Driven Design (although we only have the Route domain)
- Dependencies going inwards.

### Chosen algorithm
- The "finder" algorithm chosen for this use case has been the Dijkstra graph algorithm, using a node map for each airport and using a reference to the available routes from this airport within the same node object. 
- Combining both strategies we can calculate the shortest path from the source point to all possible routes with a cost of O(ElogV). Besides, we are breaking the process as soon as we have reached the target and all other routes have higher accumulated distances.
- Initial Graph Node initialization has been cached, assuming that we have a static list of routes.   


### Assumptions
- For this implementation, a static Repository has been implemented because we have a static list of data and a database or a load of different scenarios was not requested.
- Cache eviction has not been implemented for the same reason.

## Executing the finder
1. Build jar package with maven:```mvn clean package```
2. Run the jar: ```java -jar target/shortest-route-finder-1.0.0-SNAPSHOT.jar```
3. Execute shell command: ```route DUB SYD``` => It will print the shortest route
4. Execute shell command: ```route SYD DUB``` => It will throw an exception [non existing route]
5. execute shell command: ```route MAD SYD``` or ```route DUB MAD``` => It will throw an exception [non existing airport]
