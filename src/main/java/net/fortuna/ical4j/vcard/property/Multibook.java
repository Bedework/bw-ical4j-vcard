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

import java.text.ParseException;
import java.util.List;

/**
 * MULTIBOOK property.
 *
 * Created on 16/05/2010
 *
 * @author Mike Douglass
 *
 */
public final class Multibook extends Property {

    public static final PropertyFactory<Multibook> FACTORY = new Factory();

    private static final long serialVersionUID = 123456789L;

    private final int value;

    /**
     * @param val number of simultaneous bookings allowed
     * @param types optional classifiers
     */
    public Multibook(final int val, final Type...types) {
        super(Id.MULTIBOOK);
        value = val;
        for (Type type : types) {
            getParameters().add(type);
        }
    }

    /**
     * Factory constructor.
     * @param params property parameters
     * @param value string representation of a property value
     */
    public Multibook(final List<Parameter> params, final String value) {
        super(Id.MULTIBOOK, params);
        this.value = Integer.parseInt(value);
    }

    /**
     * @return the value
     */
    public int getInteger() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return Strings.valueOf(value);
    }

    @Override
    public void validate() throws ValidationException {
      // TODO Auto-generated method stub

    }

    private static class Factory implements PropertyFactory<Multibook> {

        /**
         * {@inheritDoc}
         */
        public Multibook createProperty(final List<Parameter> params, final String value) {
            return new Multibook(params, value);
        }

        /**
         * {@inheritDoc}
         */
        public Multibook createProperty(final Group group, final List<Parameter> params, final String value)
                throws ParseException {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
