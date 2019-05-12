## Usage

### Creating a Graph
```
Graph graph = new Graph();
```
This will create a new instance of the in memory implementation of the Graph interface. Now it can be populated with
nodes, assignments, and associations.

```
graph.createNode(1234, "newNode", NodeType.O, null));
```
This will add a node with the ID 1234, name "newNode", type object, and no properties to the graph.

```
graph.assign(1234, NodeType.O), 4321, NodeType.OA));
```
Assuming a node with the ID 4321 and type OA have been created in the graph, this will assign the node with ID 1234 to the 
node with ID 4321.

```
graph.associate(2222, NodeType.UA), 4321, NodeType.OA), new HashSet<>(Arrays.asList("read", "write")));
```
Assuming a user attribute node is created with the ID 2222, this will associate it with the object attribute that has the 
ID 4321, and give the operations read and write.

**Examples of creating graphs are provided [below](#examples)**

### Access Decisions
To make an access decision, instantiate a `PReviewDecider` which implements the `Decider` interface. The interface provides
several methods to query the current access state of the graph.
```
Decider decider = new PReviewDecider(graph);
decider.listPermissions(userID, NO_PROCESS, targetID);
```
The `listPermissions` method returns the permissions a user has on a target node.

### Audit
#### Explain
Explain answers the question why does a user have access to a given target node? To perform this, instantiate a new 
`PReviewAuditor` and call the `explain` method.
```
Auditor auditor = new PReviewAuditor(graph);
Map<String, List<Path>> explain = auditor.explain(1234, 4321);
```
The result of the explain method is a map of policy classes to the paths from the user to the target under each policy class.
The `Path` object contains a list of `Edge` objects and represents a series of paths from the user to the target. An `Edge` has
a source node, a target node, and a set of operations. If the operations is null, the edge represents an assignment in the
path. If it is not null then the edge is an association (the bridge from the user path to the target path).

See an example [below](#explain-example)