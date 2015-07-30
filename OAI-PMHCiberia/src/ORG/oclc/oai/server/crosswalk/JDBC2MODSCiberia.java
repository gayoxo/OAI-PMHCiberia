/**
 * Copyright 2006 OCLC Online Computer Library Center Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ORG.oclc.oai.server.crosswalk;

import java.util.HashMap;
import java.util.Properties;

import ORG.oclc.oai.util.OAIUtil;

/**
 * Convert native "item" to oai_dc. In this case, the native "item"
 * is assumed to already be formatted as an OAI <record> element,
 * with the possible exception that multiple metadataFormats may
 * be present in the <metadata> element. The "crosswalk", merely
 * involves pulling out the one that is requested.
 */
public class JDBC2MODSCiberia extends Crosswalk {
    private String separator = null;
    private String modsTitleInfo = null;
    private String modsNameInfo = null;
    private String modsgenreInfo = null;
    private String modsabstract = null;
	private String modstypeOfResource;
	private String modsoriginInfo;
	private String modslanguage;
	private String modsphysicalDescription;
	private String modstableOfContents;
	private String modstargetAudience;
	private String modsnote;
	private String modssubject;
	private String modsclassification;
	private String modsrelatedItem;
	private String modsidentifier;
	private String modslocation;
	private String modsaccessCondition;
	private String modspart;
	private String modsextension;
	private String modsrecordInfo;
    
    
    /**
     * The constructor assigns the schemaLocation associated with this crosswalk. Since
     * the crosswalk is trivial in this case, no properties are utilized.
     *
     * @param properties properties that are needed to configure the crosswalk.
     */
    public JDBC2MODSCiberia(Properties properties) {
        super("http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");
        
        modsTitleInfo = (String)properties.get("JDBC2MODS.modsTitleInfoLabel");
        modsNameInfo = (String)properties.get("JDBC2MODS.modsNameLabel");
        modstypeOfResource = (String)properties.get("JDBC2MODS.modstypeOfResourceLabel");
        modsgenreInfo = (String)properties.get("JDBC2MODS.modsgenreLabel");
        modsoriginInfo = (String)properties.get("JDBC2MODS.modsoriginInfoLabel");
        modslanguage = (String)properties.get("JDBC2MODS.modslanguageLabel");
        modsphysicalDescription = (String)properties.get("JDBC2MODS.modsphysicalDescriptionLabel");
        modsabstract = (String)properties.get("JDBC2MODS.modsabstractLabel");
        modstableOfContents = (String)properties.get("JDBC2MODS.modstableOfContentsLabel");
        modstargetAudience = (String)properties.get("JDBC2MODS.modstargetAudienceLabel");
        modsnote = (String)properties.get("JDBC2MODS.modsnoteLabel");
        modssubject = (String)properties.get("JDBC2MODS.modssubjectLabel");
        modsclassification = (String)properties.get("JDBC2MODS.modsclassificationLabel");
        modsrelatedItem = (String)properties.get("JDBC2MODS.modsrelatedItemLabel");
        modsidentifier = (String)properties.get("JDBC2MODS.modsidentifierLabel");
        modslocation = (String)properties.get("JDBC2MODS.modslocationLabel");
        modsaccessCondition= (String)properties.get("JDBC2MODS.modsaccessConditionLabel");
        modspart = (String)properties.get("JDBC2MODS.modspartLabel");
        modsextension = (String)properties.get("JDBC2MODS.modsextensionLabel");
        modsrecordInfo = (String)properties.get("JDBC2MODS.modsrecordInfoLabel");

        
        separator = (String)properties.get("JDBC2MODS.separator");
  
    }
    
    /**
     * Can this nativeItem be represented in DC format?
     * @param nativeItem a record in native format
     * @return true if DC format is possible, false otherwise.
     */
    public boolean isAvailableFor(Object nativeItem) {
        return true; // all records must support oai_dc according to the OAI spec.
    }
    
    /**
     * Perform the actual crosswalk.
     *
     * @param nativeItem the native "item". In this case, it is
     * already formatted as an OAI <record> element, with the
     * possible exception that multiple metadataFormats are
     * present in the <metadata> element.
     * @return a String containing the XML to be stored within the <metadata> element.
     */
    public String createMetadata(Object nativeItem) {
        HashMap table = (HashMap)nativeItem;
        StringBuffer sb = new StringBuffer();
        sb.append("<mods xmlns=\"http://www.loc.gov/mods/v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.loc.gov/mods/v3 http://www.loc.gov/standards/mods/v3/mods-3-1.xsd\">\n");
        
        sb.append(getElements(table, modsTitleInfo));
        sb.append(getElements(table, modsNameInfo));
        sb.append(getElements(table, modstypeOfResource));
        sb.append(getElements(table, modsgenreInfo));
        sb.append(getElements(table, modsoriginInfo));
        sb.append(getElements(table, modslanguage));
        sb.append(getElements(table, modsphysicalDescription));
        sb.append(getElements(table, modsabstract));
        sb.append(getElements(table, modstableOfContents));
        sb.append(getElements(table, modstargetAudience));
        sb.append(getElements(table, modsnote));
        sb.append(getElements(table, modssubject));
        sb.append(getElements(table, modsclassification));
        sb.append(getElements(table, modsrelatedItem));
        sb.append(getElements(table, modsidentifier));
        sb.append(getElements(table, modslocation));
        sb.append(getElements(table, modsaccessCondition));
        sb.append(getElements(table, modspart));
        sb.append(getElements(table, modsextension));
        sb.append(getElements(table, modsrecordInfo));
       
        sb.append("</mods>\n");
        return sb.toString();
    }
    
    private String getElements(HashMap table, String jdbcLabel) {
        StringBuffer sb = new StringBuffer();
        Object jdbcObject;
        if (jdbcLabel != null
                && (jdbcObject = table.get(jdbcLabel)) != null
                && jdbcObject.toString().length() > 0) {
            if (separator != null && separator.length() > 0) {
                String[] values = jdbcObject.toString().split(separator);
                for (int i=0; i<values.length; ++i) {
               //     sb.append("<").append(elementLabel).append(extra).append(">");
               // 	sb.append(values[i]);
                    sb.append(OAIUtil.xmlEncode(values[i]));
               //     sb.append("</").append(elementLabel).append(">\n");
                }
            } else {
               // sb.append("<").append(elementLabel).append(">");
               //sb.append(jdbcObject.toString());
            	sb.append(OAIUtil.xmlEncode(jdbcObject.toString()));
               // sb.append("</").append(elementLabel).append(">\n");
            }
        }
        return sb.toString();
    }
}
