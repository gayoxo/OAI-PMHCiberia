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
public class JDBC2MODS extends Crosswalk {
    private String separator = null;
    private String modsTitleInfo = null;
    private String modsNameInfo = null;
    private String modsgenreInfo = null;
    private String modsabstract = null;
    
    
    /**
     * The constructor assigns the schemaLocation associated with this crosswalk. Since
     * the crosswalk is trivial in this case, no properties are utilized.
     *
     * @param properties properties that are needed to configure the crosswalk.
     */
    public JDBC2MODS(Properties properties) {
        super("http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");
        
        modsTitleInfo = (String)properties.get("JDBC2MODS.modsTitleInfoLabel");
        modsNameInfo = (String)properties.get("JDBC2MODS.modsNameLabel");
        modsgenreInfo = (String)properties.get("JDBC2MODS.modsgenreLabel");
        
        modsabstract = (String)properties.get("JDBC2MODS.modsabstractLabel");
        
        separator = (String)properties.get("JDBC2MODS.separator");
   /*     dcCreatorLabel = (String)properties.get("JDBC2oai_dc.dcCreatorLabel");
        dcSubjectLabel = (String)properties.get("JDBC2oai_dc.dcSubjectLabel");
        dcDescriptionLabel = (String)properties.get("JDBC2oai_dc.dcDescriptionLabel");
        dcPublisherLabel = (String)properties.get("JDBC2oai_dc.dcPublisherLabel");
        dcContributorLabel = (String)properties.get("JDBC2oai_dc.dcContributorLabel");
        dcDateLabel = (String)properties.get("JDBC2oai_dc.dcDateLabel");
        dcTypeLabel = (String)properties.get("JDBC2oai_dc.dcTypeLabel");
        dcFormatLabel = (String)properties.get("JDBC2oai_dc.dcFormatLabel");
        dcIdentifierLabel = (String)properties.get("JDBC2oai_dc.dcIdentifierLabel");
        dcSourceLabel = (String)properties.get("JDBC2oai_dc.dcSourceLabel");
        dcLanguageLabel = (String)properties.get("JDBC2oai_dc.dcLanguageLabel");
        dcRelationLabel = (String)properties.get("JDBC2oai_dc.dcRelationLabel");
        dcCoverageLabel = (String)properties.get("JDBC2oai_dc.dcCoverageLabel");
        dcRightsLabel = (String)properties.get("JDBC2oai_dc.dcRightsLabel");
        separator = (String)properties.get("JDBC2oai_dc.separator");
    */
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
        sb.append("<mods xmlns=\"http://www.loc.gov/mods/v3\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.loc.gov/mods/v3 http://www.loc.gov/standards/mods/v3/mods-3-1.xsd\">\n");
        
        sb.append(getElements(table, modsTitleInfo, "titleinfo", ""));
        sb.append(getElements(table, modsNameInfo, "name", ""));
        sb.append(getElements(table, modsgenreInfo, "genre", ""));
        
        sb.append(getElements(table, modsabstract, "abstract", ""));
        
        sb.append("</mods>\n");
        return sb.toString();
    }
    
    private String getElements(HashMap table, String jdbcLabel, String elementLabel, String extra) {
        StringBuffer sb = new StringBuffer();
        Object jdbcObject;
        if (jdbcLabel != null
                && (jdbcObject = table.get(jdbcLabel)) != null
                && jdbcObject.toString().length() > 0) {
            if (separator != null && separator.length() > 0) {
                String[] values = jdbcObject.toString().split(separator);
                for (int i=0; i<values.length; ++i) {
                    sb.append("<").append(elementLabel).append(extra).append(">");
                    sb.append(OAIUtil.xmlEncode(values[i]));
                    sb.append("</").append(elementLabel).append(">\n");
                }
            } else {
                sb.append("<").append(elementLabel).append(">");
                sb.append(OAIUtil.xmlEncode(jdbcObject.toString()));
                sb.append("</").append(elementLabel).append(">\n");
            }
        }
        return sb.toString();
    }
}
