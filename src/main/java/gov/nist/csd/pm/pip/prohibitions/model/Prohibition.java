package gov.nist.csd.pm.pip.prohibitions.model;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;

import java.util.*;

/**
 * Object representing a Prohibition.
 */
public class Prohibition {

    private String       name;
    private String      subject;
    private Set<ContainerCondition>   containers;
    private OperationSet operations;
    private boolean      intersection;

    private Prohibition(String name, String subject, Set<ContainerCondition> containers, OperationSet operations, boolean intersection) {
        if (subject == null) {
            throw new IllegalArgumentException("Prohibition subject cannot be null");
        }

        this.name = name;
        this.subject = subject;

        if (containers == null) {
            this.containers = new HashSet<>();
        } else {
            this.containers = containers;
        }

        if (operations == null) {
            this.operations = new OperationSet();
        } else {
            this.operations = operations;
        }

        this.intersection = intersection;
    }

    public String getSubject() {
        return subject;
    }

    public Set<ContainerCondition> getContainers() {
        return containers;
    }

    public String getName() {
        return name;
    }

    public OperationSet getOperations() {
        return operations;
    }

    public boolean isIntersection() {
        return intersection;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Prohibition)) {
            return false;
        }

        Prohibition p = (Prohibition) o;
        return this.getName().equals(p.getName());
    }

    public int hashCode() {
        return Objects.hash(name);
    }

    public static class Builder {

        private String       name;
        private String      subject;
        private Set<ContainerCondition>   containers;
        private OperationSet operations;
        private boolean      intersection;

        public Builder(String name, String subject, OperationSet operations) {
            this.name = name;
            this.subject = subject;
            this.containers = new HashSet<>();
            this.operations = operations;
            this.intersection = false;
        }

        public Builder addContainer(String container, boolean complement) {
            containers.add(new ContainerCondition(container, complement));
            return this;
        }

        public Builder setIntersection(boolean intersection) {
            this.intersection = intersection;
            return this;
        }

        public Prohibition build() {
            return new Prohibition(name, subject, containers, operations, intersection);
        }
    }
}
