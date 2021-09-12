/**
 * Copyright (c) 2012, Ben Fortuna
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  o Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 *  o Neither the name of Ben Fortuna nor the names of any other contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.fortuna.ical4j.vcard;

import net.fortuna.ical4j.model.Encodable;
import net.fortuna.ical4j.util.Strings;
import net.fortuna.ical4j.validate.ValidationException;
import net.fortuna.ical4j.vcard.Property.Id;
import net.fortuna.ical4j.vcard.property.Kind;
import net.fortuna.ical4j.vcard.property.Version;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * vCard object.
 *
 * $Id$
 *
 * Created on 21/08/2008
 *
 * @author Ben
 *
 */
public final class VCard implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4784034340843199392L;

    private final List<Property> properties;

    /**
     * Default constructor.
     */
    public VCard() {
        this(new ArrayList<Property>());
    }

    /**
     * @param properties a list of properties
     */
    public VCard(final List<Property> properties) {
        this.properties = new CopyOnWriteArrayList<Property>(properties);
    }

    /**
     * Returns a reference to the list of properties for the VCard instance. Note that
     * any changes to this list are reflected in the VCard object list.
     * @return the properties
     */
    public List<Property> getProperties() {
        return properties;
    }

    /**
     * Returns a list of properties for the VCard instance with a matching identifier. Any modifications
     * to this list will not affect the list referenced by the VCard instance.
     * @param id a property identifier
     * @return a list of properties matching the specified identifier
     */
    public List<Property> getProperties(final Id id) {
        final List<Property> matches = new ArrayList<Property>();
        for (Property p : properties) {
            if (p.getId().equals(id)) {
                matches.add(p);
            }
        }
        return Collections.unmodifiableList(matches);
    }

    /**
     * Returns the first property found matching the specified identifier.
     * @param id a property identifier
     * @return the first matching property, or null if no properties match
     */
    public Property getProperty(final Id id) {
        for (Property p : properties) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns a list of non-standard properties for the VCard instance with a matching name. Any modifications
     * to this list will not affect the list referenced by the VCard instance.
     * @param name a non-standard property name
     * @return a list of non-standard properties matching the specified name
     */
    public List<Property> getExtendedProperties(final String name) {
        final List<Property> matches = new ArrayList<Property>();
        for (Property p : properties) {
            if (p.getId().equals(Id.EXTENDED) && p.extendedName.equals(name)) {
                matches.add(p);
            }
        }
        return Collections.unmodifiableList(matches);
    }

    /**
     * Returns the first non-standard property found matching the specified name.
     * @param name a non-standard property name
     * @return the first matching property, or null if no properties match
     */
    public Property getExtendedProperty(final String name) {
        for (Property p : properties) {
            if (p.getId().equals(Id.EXTENDED) && p.extendedName.equals(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * @throws ValidationException where validation fails
     */
    public void validate() throws ValidationException {
        // ;A vCard object MUST include the VERSION and FN properties.
        assertOne(Property.Id.VERSION);
        assertOne(Property.Id.FN);
        //assertOne(Property.Id.N);

        boolean isKindGroup = false;

        final List<Property> properties = getProperties(Id.KIND);
        if (properties.size() > 1) {
            throw new ValidationException("Property [" + Id.KIND + "] must be specified zero or once");
        } else if (properties.size() == 1) {
        	isKindGroup = properties.iterator().next().getValue().equals(Kind.GROUP.getValue());
        }

        for (Property property : getProperties()) {
            if (!isKindGroup && (property.getId().equals(Id.MEMBER))) {
                throw new ValidationException("Property [" + Id.MEMBER +
                		"] can only be specified if the KIND property value is \"group\".");
            }
            property.validate();
        }
    }


    /**
     * @param propertyId
     * @throws ValidationException
     */
    private void assertOne(final Property.Id propertyId) throws ValidationException {
        final List<Property> properties = getProperties(propertyId);
        if (properties.size() != 1) {
            throw new ValidationException("Property [" + propertyId + "] must be specified once");
        }
    }

    /**
     * @return a vCard-compliant string representation of the vCard object
     */
    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        b.append("BEGIN:VCARD");
        b.append(Strings.LINE_SEPARATOR);

        /* Version should come before anything else. */
        boolean version4 = true;

        Version v = (Version)getProperty(Property.Id.VERSION);

        if (v != null) {
          if (!v.equals(Version.VERSION_4_0)) {
            version4 = false; // Treat it as 3
          }
          b.append(v);
//        } else {
  //        b.append(Version.VERSION_4_0);
        }

        for (Property prop : properties) {
            if (prop.getId() == Property.Id.VERSION) {
              continue;
            }

            if (version4) {
              b.append(prop);
              continue;
            }

            /* Attempt to downgrade by turning some properties into x-props. */
            appendDowngraded(b, prop);
        }

        b.append("END:VCARD");
        b.append(Strings.LINE_SEPARATOR);
        return b.toString();
    }

    public static final String v4AsXpropPrefix = "X-ICAL4J-TOV3-";

    private void appendDowngraded(final StringBuilder b,
                                  final Property prop) {
      /* From rfc6350 - vcard 4.0

Appendix A. Differences from RFCs 2425 and 2426
   This appendix contains a high-level overview of the major changes
   that have been made in the vCard specification from RFCs 2425 and
   2426.  It is incomplete, as it only lists the most important changes.

A.1. New Structure
   o  [RFC2425] and [RFC2426] have been merged.

   o  vCard is now not only a MIME type but a stand-alone format.

   o  A proper MIME type registration form has been included.

   o  UTF-8 is now the only possible character set.

   o  New vCard elements can be registered from IANA.

A.2. Removed Features
   o  The CONTEXT and CHARSET parameters are no more.

   o  The NAME, MAILER, LABEL, and CLASS properties are no more.

   o  The "intl", "dom", "postal", and "parcel" TYPE parameter values
      for the ADR property have been removed.

   o  In-line vCards (such as the value of the AGENT property) are no
      longer supported.

A.3. New Properties and Parameters
   o  The KIND, GENDER, LANG, ANNIVERSARY, XML, and CLIENTPIDMAP
      properties have been added.

   o  [RFC2739], which defines the FBURL, CALADRURI, CAPURI, and CALURI
      properties, has been merged in.

   o  [RFC4770], which defines the IMPP property, has been merged in.

   o  The "work" and "home" TYPE parameter values are now applicable to
      many more properties.

   o  The "pref" value of the TYPE parameter is now a parameter of its
      own, with a positive integer value indicating the level of
      preference.

   o  The ALTID and PID parameters have been added.

   o  The MEDIATYPE parameter has been added and replaces the TYPE
      parameter when it was used for indicating the media type of the
      property's content.
      */

	if (v3Ok(prop)) {
	    b.append(prop);
	    return;
	}

	/* x-prop already? */

	if (Property.Id.EXTENDED == prop.getId()) {
	    b.append(prop);
	    return;
	}

	/* Output as x-prop */
        if (prop.getGroup() != null) {
            b.append(prop.getGroup());
            b.append('.');
        }

        b.append(v4AsXpropPrefix + prop.getId().getPropertyName());

        for (Parameter param : prop.getParameters()) {
            b.append(';');

            /* Watch for non v3 */
            b.append(param);
        }
        b.append(':');

        if (prop instanceof Encodable) {
            b.append(Strings.escape(Strings.valueOf(prop.getValue())));
        }
        else {
            b.append(Strings.valueOf(prop.getValue()));
        }

        b.append(Strings.LINE_SEPARATOR);
    }

    private static Set<Property.Id> notV3Ok = new TreeSet<Property.Id>();

    static {
      notV3Ok.add(Property.Id.KIND);
      notV3Ok.add(Property.Id.GENDER);
      notV3Ok.add(Property.Id.LANG);
      notV3Ok.add(Property.Id.ANNIVERSARY);
      notV3Ok.add(Property.Id.XML);
      notV3Ok.add(Property.Id.CLIENTPIDMAP);
    }

    /* Return true if this property should be OK for v3 */
    private boolean v3Ok(final Property prop) {
      Property.Id id = prop.getId();

      if (notV3Ok.contains(id)) {
	  return false;
      }

      Parameter par = prop.getParameter(Parameter.Id.ALTID);
      if (par != null) {
        return false;
      }

      par = prop.getParameter(Parameter.Id.PID);
      if (par != null) {
        return false;
      }

      return true;
    }
}
