/*
 * Hibernate OGM, Domain model persistence for NoSQL datastores
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.ogm.datastore.cassandra.type.impl;

import org.hibernate.ogm.model.spi.Tuple;
import org.hibernate.ogm.type.descriptor.impl.BasicGridBinder;
import org.hibernate.ogm.type.descriptor.impl.GridTypeDescriptor;
import org.hibernate.ogm.type.descriptor.impl.GridValueBinder;
import org.hibernate.ogm.type.descriptor.impl.GridValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;

/**
 * Cassandra doesn't support the {@code Character} type natively,
 * so we wrap characters into strings.
 *
 * @author Nicola Ferraro
 */
public class CharacterStringGridTypeDescriptor implements GridTypeDescriptor {
	public static final CharacterStringGridTypeDescriptor INSTANCE = new CharacterStringGridTypeDescriptor();

	@Override
	public <X> GridValueBinder<X> getBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
		return new BasicGridBinder<X>( javaTypeDescriptor, this ) {

			@Override
			protected void doBind(Tuple resultset, X value, String[] names, WrapperOptions options) {
				Character ch = javaTypeDescriptor.unwrap( value, Character.class, options );
				resultset.put( names[0], String.valueOf( ch ) );
			}
		};
	}

	@Override
	public <X> GridValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
		return new GridValueExtractor<X>() {

			@Override
			public X extract(Tuple resultset, String name) {

				X result = (X) resultset.get( name );

				if ( result == null ) {
					return null;
				}
				else {
					String chString = (String) result;
					Character ch = chString.charAt( 0 );
					return javaTypeDescriptor.wrap( ch, null );
				}
			}
		};
	}

}