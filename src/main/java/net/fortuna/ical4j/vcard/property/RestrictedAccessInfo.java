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
package net.fortuna.ical4j.vcard.property;

import net.fortuna.ical4j.util.Strings;
import net.fortuna.ical4j.validate.ValidationException;
import net.fortuna.ical4j.vcard.Group;
import net.fortuna.ical4j.vcard.Parameter;
import net.fortuna.ical4j.vcard.Property;
import net.fortuna.ical4j.vcard.PropertyFactory;
import net.fortuna.ical4j.vcard.parameter.Type;
import net.fortuna.ical4j.vcard.parameter.Value;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

/**
 * RESTRICTEDACCESSINFO property.
 *
 * Created on 16/05/2010
 *
 * @author Mike Douglass
 *
 */
public final class RestrictedAccessInfo extends Property {

    public static final PropertyFactory<RestrictedAccessInfo> FACTORY = new Factory();

    private static final long serialVersionUID = 123L;

    private URI uri;

    private String value;

    /**
     * @param uri Restricted access info URI value
     * @param types optional classifiers
     */
    public RestrictedAccessInfo(final URI uri, final Type...types) {
        super(Id.RESTRICTEDACCESSINFO);
        this.uri = uri;
        for (Type type : types) {
            getParameters().add(type);
        }
    }

    /**
     * @param val Restricted access info free text value
     * @param types optional classifiers
     * @throws URISyntaxException  where the specified string value is not a valid URI
     *       and the type is "uri"
     */
    public RestrictedAccessInfo(final String val, final Type...types) throws URISyntaxException {
        super(Id.RESTRICTEDACCESSINFO);
        value = val;
        for (Type type : types) {
            getParameters().add(type);
        }
    }

    /**
     * Factory constructor.
     * @param params property parameters
     * @param value string representation of a property value
     * @throws URISyntaxException where the specified string value is not a valid URI
     */
    public RestrictedAccessInfo(final List<Parameter> params, final String value) throws URISyntaxException {
        super(Id.RESTRICTEDACCESSINFO, params);

        Value v = (Value) this.getParameter(Parameter.Id.VALUE);
        if ((v == null) | (v.getValue().toLowerCase().equals("uri"))) {
	    uri = new URI(value);
	} else {
	    this.value = value;
	}
    }

    /**
     * @return the uri
     */
    public URI getUri() {
        return uri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
	if (value != null) {
	    return value;
	}

        return Strings.valueOf(uri);
    }

    @Override
    public void validate() throws ValidationException {
      // TODO Auto-generated method stub

    }

    private static class Factory implements PropertyFactory<RestrictedAccessInfo> {

        /**
         * {@inheritDoc}
         */
        public RestrictedAccessInfo createProperty(final List<Parameter> params, final String value) throws URISyntaxException {
            return new RestrictedAccessInfo(params, value);
        }

        /**
         * {@inheritDoc}
         */
        public RestrictedAccessInfo createProperty(final Group group, final List<Parameter> params, final String value)
                throws URISyntaxException, ParseException {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
