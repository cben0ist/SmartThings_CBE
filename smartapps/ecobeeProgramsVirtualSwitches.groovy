{\rtf1\ansi\ansicpg1252\cocoartf1561
{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx566\tx1133\tx1700\tx2267\tx2834\tx3401\tx3968\tx4535\tx5102\tx5669\tx6236\tx6803\pardirnatural\partightenfactor0

\f0\fs24 \cf0 /**\
 *  Ecobee programs virtual switches\
 *\
 *  Current Version: 1.0\
 *\
 */\
        \
definition(\
	name: "Ecobee programs virtual switches",\
	namespace: "cben0ist",\
	author: "cben0ist",\
	description: "Expose virtual switch for ecobee programs",\
	category: "My Apps",\
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Partner/ecobee.png",\
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Partner/ecobee@2x.png"\
)\
\
preferences \{\
    section("Select the ecobee thermostat") \{\
        input "ecobee", "device.myEcobeeDevice", title: "Which ecobee thermostat?"\
    \}\
\}\
\
def installed() \{\
    log.debug "Installed..."\
	initialize()\
\}\
\
def updated() \{\
    log.debug "Updated..."\
	unsubscribe()\
	unschedule()\
	initialize()\
\}\
\
def initialize() \{\
    log.debug "Initialize..."\
    \
    def awaySwitch = createVirtualSwitch (getAwayDeviceDNI(), "Ecobee Away Mode")\
    subscribe(awaySwitch, "switch", awayHandler)\
    \
    def nightSwitch = createVirtualSwitch (getNightDeviceDNI(), "Ecobee Night Mode")\
    subscribe(nightSwitch, "switch", nightHandler)\
    \
    subscribe(ecobee, "programNameForUI", ecobeeHandler)\
\}\
\
private def createVirtualSwitch (dni, stringName) \{\
    def d = getChildDevice(dni)\
    if(!d) \{\
        d = addChildDevice("cben0ist", "Virtual Switch", dni, null, [name:stringName, label:name])\
        d.take()\
        log.debug "created $\{d.displayName\} with id $dni"\
    \} else \{\
        log.debug "dni already in use"\
    \}\
    return d\
\}\
\
def ecobeeHandler(evt) \{\
    def nightSwitch = getChildDevice(getNightDeviceDNI())\
	def nightSwitchValue = nightSwitch.currentValue("switch")\
    \
    def awaySwitch = getChildDevice(getAwayDeviceDNI())\
	def awaySwitchValue = awaySwitch.currentValue("switch")\
    \
    log.debug "ecobeeHandler... $evt.value and $nightSwitchValue and $awaySwitchValue"\
    \
	if(evt.value == "Sleep" && nightSwitchValue != "on")\{\
        nightSwitch.on()\
    \}\
	if(evt.value != "Sleep" && nightSwitchValue == "on")\{\
        nightSwitch.off()\
    \}\
    \
	if(evt.value == "Away" && awaySwitchValue != "on")\{\
        log.debug "ecobeeHandler... Turning away switch on"\
        awaySwitch.on()\
    \}\
	if(evt.value != "Away" && awaySwitchValue == "on")\{\
    	log.debug "ecobeeHandler... Turning away switch off"\
        awaySwitch.off()\
    \}\
\}\
\
def awayHandler(evt) \{\
	def currentProgram = ecobee.currentValue("programNameForUI")\
    log.debug "awayHandler... $evt.value and $currentProgram"\
    \
	if(evt.value.contains("on") && currentProgram != "Away")\{\
    	log.debug "awayHandler...Setting Ecobee Away"\
        ecobee.away()\
    \} \
    \
	if(evt.value.contains("off") && currentProgram != "Home")\{\
    	log.debug "awayHandler...Resume Ecobee"\
        ecobee.resumeThisTstat()\
    \}\
\}\
\
def nightHandler(evt) \{\
	def currentProgram = ecobee.currentValue("programNameForUI")\
    log.debug "nightHandler... $evt.value"\
    \
	if(evt.value.contains("on") && currentProgram != "Sleep")\{\
        ecobee.setThisTstatClimate("Sleep")\
    \} \
    \
	if(evt.value.contains("off") && currentProgram != "Home")\{\
        ecobee.resumeThisTstat()\
    \}\
\}\
\
private def getAwayDeviceDNI() \{\
	return "cben0ist_Ecobee_Away_Mode"\
\}\
\
private def getNightDeviceDNI() \{\
	return "cben0ist_Ecobee_Night_Mode"\
\}\
}