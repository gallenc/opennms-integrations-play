#
# PySNMP MIB module CHUBB-ROOT (http://snmplabs.com/pysmi)
# ASN.1 source file:///usr/local/snmpsim/mibs/CHUBB-ROOT.mib
# Produced by pysmi-0.3.1 at Mon Aug 15 13:44:30 2022
# On host snmpsim platform Linux version 5.10.102.1-microsoft-standard-WSL2 by user root
# Using Python version 3.4.9 (default, Sep  5 2018, 04:27:05)
#
Integer, ObjectIdentifier, OctetString = mibBuilder.importSymbols("ASN1", "Integer", "ObjectIdentifier", "OctetString")
NamedValues, = mibBuilder.importSymbols("ASN1-ENUMERATION", "NamedValues")
ConstraintsUnion, SingleValueConstraint, ValueRangeConstraint, ConstraintsIntersection, ValueSizeConstraint = mibBuilder.importSymbols("ASN1-REFINEMENT", "ConstraintsUnion", "SingleValueConstraint", "ValueRangeConstraint", "ConstraintsIntersection", "ValueSizeConstraint")
ModuleCompliance, NotificationGroup = mibBuilder.importSymbols("SNMPv2-CONF", "ModuleCompliance", "NotificationGroup")
NotificationType, Unsigned32, Counter64, MibIdentifier, ModuleIdentity, Integer32, Counter32, MibScalar, MibTable, MibTableRow, MibTableColumn, iso, Bits, ObjectIdentity, Gauge32, IpAddress, TimeTicks, enterprises = mibBuilder.importSymbols("SNMPv2-SMI", "NotificationType", "Unsigned32", "Counter64", "MibIdentifier", "ModuleIdentity", "Integer32", "Counter32", "MibScalar", "MibTable", "MibTableRow", "MibTableColumn", "iso", "Bits", "ObjectIdentity", "Gauge32", "IpAddress", "TimeTicks", "enterprises")
DisplayString, TextualConvention = mibBuilder.importSymbols("SNMPv2-TC", "DisplayString", "TextualConvention")
chubb = ModuleIdentity((1, 3, 6, 1, 4, 1, 52330))
chubb.setRevisions(('2020-01-10 12:00', '2018-07-03 12:00',))
if mibBuilder.loadTexts: chubb.setLastUpdated('202001101200Z')
if mibBuilder.loadTexts: chubb.setOrganization('Chubb Systems Ltd')
products = ObjectIdentity((1, 3, 6, 1, 4, 1, 52330, 1))
if mibBuilder.loadTexts: products.setStatus('current')
mibBuilder.exportSymbols("CHUBB-ROOT", chubb=chubb, PYSNMP_MODULE_ID=chubb, products=products)