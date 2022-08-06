#
# PySNMP MIB module CHUBB-ROOT (http://snmplabs.com/pysmi)
# ASN.1 source file:///usr/local/snmpsim/mibs/CHUBB-ROOT.mib
# Produced by pysmi-0.3.4 at Thu Jun 16 16:06:24 2022
# On host snmpsim platform Linux version 5.10.102.1-microsoft-standard-WSL2 by user root
# Using Python version 3.4.9 (default, Sep  5 2018, 04:27:05)
#
ObjectIdentifier, Integer, OctetString = mibBuilder.importSymbols("ASN1", "ObjectIdentifier", "Integer", "OctetString")
NamedValues, = mibBuilder.importSymbols("ASN1-ENUMERATION", "NamedValues")
ConstraintsUnion, SingleValueConstraint, ValueSizeConstraint, ValueRangeConstraint, ConstraintsIntersection = mibBuilder.importSymbols("ASN1-REFINEMENT", "ConstraintsUnion", "SingleValueConstraint", "ValueSizeConstraint", "ValueRangeConstraint", "ConstraintsIntersection")
NotificationGroup, ModuleCompliance = mibBuilder.importSymbols("SNMPv2-CONF", "NotificationGroup", "ModuleCompliance")
iso, Unsigned32, Counter64, Gauge32, ModuleIdentity, TimeTicks, Counter32, IpAddress, enterprises, MibIdentifier, MibScalar, MibTable, MibTableRow, MibTableColumn, Bits, ObjectIdentity, Integer32, NotificationType = mibBuilder.importSymbols("SNMPv2-SMI", "iso", "Unsigned32", "Counter64", "Gauge32", "ModuleIdentity", "TimeTicks", "Counter32", "IpAddress", "enterprises", "MibIdentifier", "MibScalar", "MibTable", "MibTableRow", "MibTableColumn", "Bits", "ObjectIdentity", "Integer32", "NotificationType")
TextualConvention, DisplayString = mibBuilder.importSymbols("SNMPv2-TC", "TextualConvention", "DisplayString")
chubb = ModuleIdentity((1, 3, 6, 1, 4, 1, 52330))
chubb.setRevisions(('2020-01-10 12:00', '2018-07-03 12:00',))
if mibBuilder.loadTexts: chubb.setLastUpdated('202001101200Z')
if mibBuilder.loadTexts: chubb.setOrganization('Chubb Systems Ltd')
products = ObjectIdentity((1, 3, 6, 1, 4, 1, 52330, 1))
if mibBuilder.loadTexts: products.setStatus('current')
mibBuilder.exportSymbols("CHUBB-ROOT", products=products, PYSNMP_MODULE_ID=chubb, chubb=chubb)
