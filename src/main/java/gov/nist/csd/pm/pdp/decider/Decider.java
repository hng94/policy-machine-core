package gov.nist.csd.pm.pdp.decider;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Interface for making access decisions.
 */
public interface Decider {

    /**
     * Check if the subject has the permissions on the target node. Use '*' as the permission to check
     * if the subject has any permissions on the node.
     *
     * @param subjectID    the ID of the subject.
     * @param processID    the ID of the process if applicable.
     * @param targetID  the ID of the target node.
     * @param perms     the array of permission sto check for.
     * @return true if the subject has the permissions on the target node, false otherwise.
     * @throws PMException if there is an exception traversing the graph.
     */
    boolean check(long subjectID, long processID, long targetID, String... perms) throws PMException;

    /**
     * List the permissions that the subject has on the target node.
     *
     * @param subjectID    the ID of the subject.
     * @param processID    the ID of the process if applicable.
     * @param targetID  the ID of the target node.
     * @return the set of operations that the subject is allowed to perform on the target.
     * @throws PMException if there is an exception traversing the graph.
     */
    Set<String> list(long subjectID, long processID, long targetID) throws PMException;

    /**
     * Given a list of nodes filter out any nodes that the given subject does not have the given permissions on. To filter
     * based on any permissions use Operations.ANY as the permission to check for.
     *
     * @param subjectID    the ID of the subject.
     * @param processID    the ID of the process if applicable.
     * @param nodes     the nodes to filter from.
     * @param perms     the permissions to check for.
     * @return a subset of the given nodes that the subject has the given permissions on.
     */
    Collection<Long> filter(long subjectID, long processID, Collection<Long> nodes, String... perms) throws PMException;

    /**
     * Get the children of the target node that the subject has the given permissions on.
     *
     * @param subjectID    the ID of the subject.
     * @param processID    the ID of the process if applicable.
     * @param targetID  the ID of the target node.
     * @param perms     the permissions the subject must have on the child nodes.
     * @return the set of NGACNodes that are children of the target node and the subject has the given permissions on.
     * @throws PMException if there is an exception traversing the graph.
     */
    Collection<Long> getChildren(long subjectID, long processID, long targetID, String... perms) throws PMException;

    /**
     * Given a subject ID, return every node the subject has access to and the permissions they have on each.
     *
     * @param subjectID the ID of the subject.
     * @param processID    the ID of the process if applicable.
     * @return a map of nodes the subject has access to and the permissions on each.
     * @throws PMException if there is an error traversing the graph.
     */
    Map<Long, Set<String>> getAccessibleNodes(long subjectID, long processID) throws PMException;

    /**
     * Given an Object Attribute ID, returns the id of every user (long), and what permissions(Set<String>) it has on it
     *
     * @param oaID
     * @param processID
     * @return
     */
    Map<Long, OperationSet> generateACL(long oaID, long processID);
}


