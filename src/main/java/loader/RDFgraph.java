/*
* Copyright (C) 2017 University of Freiburg.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* The RDFPath Engine is a research project at the department of 
* computer science, University of Freiburg. 
*
* More information on the project:
* http://dbis.informatik.uni-freiburg.de/forschung/projekte/DiPoS/
* zablocki@informatik.uni-freiburg.de
*/
package loader;

import java.io.Serializable;

public class RDFgraph implements Serializable {
	private static final long serialVersionUID = 1L;
	private String subject;
	   private String predicate;
	   private String object;

	   public String getSubject() {
	     return subject;
	   }

	   public void setSubject(String subject) {
	     this.subject = subject;
	   }
	   
	   public String getPredicate() {
		     return predicate;
		   }

		public void setPredicate(String predicate) {
		     this.predicate = predicate;
		   } 
		public String getObject() {
		     return object;
			   }

		public void setObject(String object) {
		     this.object = object;
			   }
}
