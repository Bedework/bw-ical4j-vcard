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

import net.fortuna.ical4j.model.Encodable;
import net.fortuna.ical4j.validate.ValidationException;
import net.fortuna.ical4j.vcard.Group;
import net.fortuna.ical4j.vcard.Parameter;
import net.fortuna.ical4j.vcard.Property;
import net.fortuna.ical4j.vcard.PropertyFactory;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import static net.fortuna.ical4j.util.Strings.unescape;

/**
 * Xproperty property.
 *
 * $Id$
 *
 * Created on 22/08/2008
 *
 * @author douglm
 *
 */
public final class Xproperty extends Property implements Encodable {

    public static final PropertyFactory<Xproperty> FACTORY =
            new ExtendedFactory();

    private static final long serialVersionUID = -3524639290151277814L;

    private final String value;

    /**
     * @param extendedName the extended name
     * @param value a name value
     */
    public Xproperty(final String extendedName,
                     final String value) {
        super(extendedName);
        this.value = value;
    }

    /**
     * Factory constructor.
     * @param extendedName the extended name
     * @param params property parameters
     * @param value string representation of a property value
     */
    public Xproperty(final String extendedName,
                     final List<Parameter> params,
                     final String value) {
        super(extendedName, params);
        this.value = value;
    }

    /**
     * Factory constructor.
     * @param group the group name
     * @param extendedName the extended name
     * @param params property parameters
     * @param value string representation of a property value
     */
    public Xproperty(final Group group,
                     final String extendedName,
                     final List<Parameter> params,
                     final String value) {
        super(group, extendedName, params);
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() throws ValidationException {
    }

    public static class ExtendedFactory implements PropertyFactory<Xproperty> {
        public Xproperty createProperty(final String extendedName,
                                        final List<Parameter> params,
                                        final String value) {
            return new Xproperty(extendedName, params, unescape(value));
        }

        public Xproperty createProperty(final Group group,
                                        final String extendedName,
                                        final List<Parameter> params,
                                        final String value)
                throws URISyntaxException, ParseException {
            return new Xproperty(group, extendedName, params, unescape(value));
        }

        /**
         * {@inheritDoc}
         */
        public Xproperty createProperty(final List<Parameter> params,
                                        final String value) {
            return new Xproperty(null, params, unescape(value));
        }

        /**
         * {@inheritDoc}
         */
        public Xproperty createProperty(final Group group,
                                        final List<Parameter> params,
                                        final String value)
                throws URISyntaxException, ParseException {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
