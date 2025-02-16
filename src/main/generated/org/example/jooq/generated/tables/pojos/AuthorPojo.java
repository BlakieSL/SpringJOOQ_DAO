/*
 * This file is generated by jOOQ.
 */
package org.example.jooq.generated.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class AuthorPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String firstName;
    private final String lastName;

    public AuthorPojo(AuthorPojo value) {
        this.id = value.id;
        this.firstName = value.firstName;
        this.lastName = value.lastName;
    }

    public AuthorPojo(
        Long id,
        String firstName,
        String lastName
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Getter for <code>example.author.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for <code>example.author.first_name</code>.
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Getter for <code>example.author.last_name</code>.
     */
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AuthorPojo other = (AuthorPojo) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.firstName == null) {
            if (other.firstName != null)
                return false;
        }
        else if (!this.firstName.equals(other.firstName))
            return false;
        if (this.lastName == null) {
            if (other.lastName != null)
                return false;
        }
        else if (!this.lastName.equals(other.lastName))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.firstName == null) ? 0 : this.firstName.hashCode());
        result = prime * result + ((this.lastName == null) ? 0 : this.lastName.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AuthorPojo (");

        sb.append(id);
        sb.append(", ").append(firstName);
        sb.append(", ").append(lastName);

        sb.append(")");
        return sb.toString();
    }
}
