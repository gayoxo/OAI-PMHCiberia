<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                              xmlns:oai="http://www.openarchives.org/OAI/2.0/">
  <xsl:output method="html" version="4.0"/>

  <xsl:template match="/oai:OAI-PMH">
    <html>
      <head><title>Table of Contents</title></head>
      <body>
        <table>
	  <xsl:apply-templates select="oai:ListIdentifiers/oai:header/oai:identifier"/>
        </table>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="oai:identifier">
    <tr>
      <td><a><xsl:attribute name="href"><xsl:value-of select="/oai:OAI-PMH/oai:request"/>?verb=GetRecord&amp;metadataPrefix=<xsl:value-of select="/oai:OAI-PMH/oai:request/@metadataPrefix"/>&amp;identifier=<xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></a></td>
    </tr>
  </xsl:template>

</xsl:stylesheet>