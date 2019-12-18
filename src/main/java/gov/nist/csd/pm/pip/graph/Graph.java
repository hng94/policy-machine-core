package gov.nist.csd.pm.pip.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interface for maintaining an NGAC graph.
 */
public interface Graph {

    /**
     * Create a new node with the given name, type and properties and add it to the graph.
     *
     * @param id the ID of the node.
     * @param name the name of the node.
     * @param type the type of node.
     * @param properties any additional properties to store in the node.
     * @return the Node representation of the newly created node.
     * @throws PMException if there is an error creating the node in the graph.
     */
    Node createNode(long id, String name, NodeType type, Map<String, String> properties) throws PMException;

    /**
     * Update the name and properties of the node with the given ID. The node's existing properties will be overwritten
     * by the ones provided. The name parameter is optional and will be ignored if null or empty.  The properties
     * parameter will be ignored only if null.  If the map is empty, the node's properties will be overwritten
     * with the empty map.
     *
     * @param id the ID of the node to update.
     * @param name the new name to give the node
     * @throws PMException if there is an error updating the node in the graph.
     */
    void updateNode(long id, String name, Map<String, String> properties) throws PMException;

    /**
     * Delete the node with the given ID from the graph.
     *
     * @param nodeID the ID of the node to delete.
     * @throws PMException if there is an error deleting the node from the graph.
     */
    void deleteNode(long nodeID) throws PMException;

    /**
     * Check that a node with the given ID exists in the graph.
     *
     * @param nodeID the ID of the node to check for.
     * @return true or False if a node with the given ID exists or not.
     * @throws PMException if there is an error checking if the node exists in the graph.
     */
    boolean exists(long nodeID) throws PMException;

    /**
     * Get the set of policy classes.  This operation is run every time a decision is made, so a separate
     * method is needed to improve efficiency. The returned set is just the IDs of each policy class.
     *
     * @return the set of policy class IDs.
     * @throws PMException if there is an error retrieving the IDs of the policy classes.
     */
    Set<Long> getPolicies() throws PMException;

    /**
     * Retrieve the set of all nodes in the graph.
     *
     * @return a Set of all the nodes in the graph.
     * @throws PMException if there is an error retrieving all nodes in the graph.
     */
    Collection<Node> getNodes() throws PMException;

    /**
     * Retrieve the node with the given ID.
     *
     * @param id the ID of the node to get.
     * @return the Node with the given ID.
     * @throws PMException if there is an error retrieving the node from the graph.
     */
    Node getNode(long id) throws PMException;

    /**
     * Search the graph for nodes matching the given parameters. A node must
     * contain all properties provided to be returned.
     * To get all the nodes that have a specific property key with any value use "*" as the value in the parameter.
     * (i.e. {key=*})
     *
     * @param name       the name of the nodes to search for.
     * @param type       the type of the nodes to search for.
     * @param properties the properties of the nodes to search for.
     * @return a set of nodes that match the given search criteria.
     * @throws PMException if there is an error searching the graph.
     */
    Set<Node> search(String name, String type, Map<String, String> properties) throws PMException;

    /**
     * Get the set of nodes that are assigned to the node with the given ID.
     *
     * @param nodeID the ID of the node to get the children of.
     * @return the Set of NGACNodes that are assigned to the node with the given ID.
     * @throws PMException if there is an error retrieving the children of the node.
     */
    Set<Long> getChildren(long nodeID) throws PMException;

    /**
     * Get the set of nodes that the node with the given ID is assigned to.
     *
     * @param nodeID the ID of the node to get the parents of.
     * @return the Set of NGACNodes that are assigned to the node with the given ID.
     * @throws PMException if there is an error retrieving the parents of the node.
     */
    Set<Long> getParents(long nodeID) throws PMException;

    /**
     * Assign the child node to the parent node. The child and parent nodes must both already exist in the graph,
     * and the types must make a valid assignment. An example of a valid assignment is assigning o1, an object, to oa1,
     * an object attribute.  o1 is the child (objects can never be the parent in an assignment), and oa1 is the parent.
     *
     * @param childID  the ID of the child node.
     * @param parentID the ID of the parent node.
     * @throws PMException if there is an error assigning the two nodes.
     */
    void assign(long childID, long parentID) throws PMException;

    /**
     * Remove the Assignment between the child and parent nodes.
     *
     * @param childID  the ID of the child node.
     * @param parentID the ID of the parent node.
     * @throws PMException if there is an error deassigning the two nodes.
     */
    void deassign(long childID, long parentID) throws PMException;

    /**
     * Create an Association between the user attribute and the Target node with the provided operations. If an association
     * already exists between these two nodes, overwrite the existing operations with the ones provided.  Associations
     * can only begin at a user attribute but can point to either an Object or user attribute
     *
     * @param uaID The ID of the user attribute.
     * @param targetID The ID of the target attribute.
     * @param operations A Set of operations to add to the association.
     * @throws PMException if there is an error associating the two nodes.
     */
    void associate(long uaID, long targetID, Set<String> operations) throws PMException;

    /**
     * Delete the Association between the user attribute and Target node.
     *
     * @param uaID     the ID of the user attribute.
     * @param targetID the ID of the target attribute.
     * @throws PMException if there is an error dissociating the two nodes.
     */
    void dissociate(long uaID, long targetID) throws PMException;

    /**
     * Retrieve the associations the given node is the source of.  The source node of an association is always a
     * user attribute and this method will throw an exception if an invalid node is provided.  The returned Map will
     * contain the target and operations of each association.
     *
     * @param sourceID the ID of the source node.
     * @return a Map of the target node IDs and the operations for each association.
     * @throws PMException if there is an retrieving the associations of the source node from the graph.
     */
    Map<Long, Set<String>> getSourceAssociations(long sourceID) throws PMException;

    /**
     * Retrieve the associations the given node is the target of.  The target node can be an Object Attribute or a User
     * Attribute. This method will throw an exception if a node of any other type is provided.  The returned Map will
     * contain the source node IDs and the operations of each association.
     *
     * @param targetID the ID of the target node.
     * @return a Map of the source Ids and the operations for each association.
     * @throws PMException if there is an retrieving the associations of the target node from the graph.
     */
    Map<Long, Set<String>> getTargetAssociations(long targetID) throws PMException;

    static String serialize(Graph graph) throws PMException {
        String s = "# nodes\n";
        Collection<Node> nodes = graph.getNodes();
        for (Node node : nodes) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";
        }

        s += "\n# assignments\n";
        for (Node node : nodes) {
            Set<Long> parents = graph.getParents(node.getID());
            for (Long parentID : parents) {
                Node parentNode = graph.getNode(parentID);
                s += "assign " + node.getType() + ":" + node.getName() + " " + parentNode.getType() + ":" + parentNode.getName() + "\n";
            }
        }

        s += "\n# associations\n";
        for (Node node : nodes) {
            Map<Long, Set<String>> assocs = graph.getSourceAssociations(node.getID());
            for (Long targetID : assocs.keySet()) {
                Node targetNode = graph.getNode(targetID);
                s += "assoc " +
                        node.getType() + ":" + node.getName() + " " +
                        targetNode.getType() + ":" + targetNode.getName() + " " +
                        assocs.get(targetID).toString()
                                .replaceAll("\\[", "").replaceAll("]", "") + "\n";
            }
        }

        return s;
    }

    static Graph deserialize(Graph graph, String str) throws PMException {
        Scanner sc = new Scanner(str);
        Random rand = new Random();
        Map<String, Long> ids = new HashMap<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }

            String[] pieces = line.split(" ");
            switch (pieces[0]) {
                case "node":
                    if (pieces.length < 3) {
                        throw new PMException("invalid node command: " + line);
                    }
                    // node <type> <name> <props>
                    String type = pieces[1];

                    String name = pieces[2];
                    int i;
                    for (i = 3; i < pieces.length; i++) {
                        String piece = pieces[i];
                        if (piece.startsWith("{")) {
                            break;
                        }

                        name += " " + piece;
                    }

                    String props = "";
                    Map<String, String> propsMap = new HashMap<>();
                    if (i == pieces.length-1) {
                        props = pieces[i];
                        props = props.replaceAll("\\{", "").replaceAll("}", "");
                        String[] propsPieces = props.split(",");
                        for (String prop : propsPieces) {
                            String[] propPieces = prop.split("=");
                            if (propPieces.length != 2) {
                                throw new PMException("invalid property format: " + line);
                            }
                            propsMap.put(propPieces[0], propPieces[1]);
                        }
                    }

                    Node node = graph.createNode(rand.nextLong(), name, NodeType.toNodeType(type), propsMap);
                    ids.put(node.getType() + ":" + node.getName(), node.getID());
                    break;
                case "assign":
                    if (pieces.length < 3) {
                        throw new PMException("invalid assign command: " + line);
                    }

                    name = pieces[1];
                    for (i = 2; i < pieces.length; i++) {
                        String piece = pieces[i];
                        if (piece.contains(":")) {
                            break;
                        }

                        name += " " + piece;
                    }
                    long childID = ids.get(name);

                    name = pieces[i];
                    i++;
                    for (int j = i; j < pieces.length; j++) {
                        String piece = pieces[j];
                        name += " " + piece;
                    }
                    long parentID = ids.get(name);

                    graph.assign(childID, parentID);

                    break;
                case "assoc":
                    if (pieces.length < 4) {
                        throw new PMException("invalid assoc command: " + line);
                    }

                    name = pieces[1];
                    for (i = 2; i < pieces.length; i++) {
                        String piece = pieces[i];
                        if (piece.contains(":")) {
                            break;
                        }

                        name += " " + piece;
                    }
                    long uaID = ids.get(name);

                    name = pieces[i];
                    i++;
                    for (int j = i; j < pieces.length; j++) {
                        String piece = pieces[j];
                        if (piece.contains("[")) {
                            break;
                        }

                        name += " " + piece;
                    }
                    long targetID = ids.get(name);

                    i++;
                    String opsStr = "";
                    for (int j = i; j < pieces.length; j++) {
                        opsStr += pieces[j].replaceAll("\\[", "").replaceAll("]", "")
                                .trim();
                    }
                    String[] ops = opsStr.split(",");
                    graph.associate(uaID, targetID, new HashSet<>(Arrays.asList(ops)));
                    break;
            }
        }

        return graph;
    }
}
