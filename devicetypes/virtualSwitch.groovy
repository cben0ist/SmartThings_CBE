{\rtf1\ansi\ansicpg1252\cocoartf1561
{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx566\tx1133\tx1700\tx2267\tx2834\tx3401\tx3968\tx4535\tx5102\tx5669\tx6236\tx6803\pardirnatural\partightenfactor0

\f0\fs24 \cf0 /**\
 *  Virtual Switch\
 *\
 *  Copyright 2014 Juan Risso\
 *\
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except\
 *  in compliance with the License. You may obtain a copy of the License at:\
 *\
 *      http://www.apache.org/licenses/LICENSE-2.0\
 *\
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed\
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License\
 *  for the specific language governing permissions and limitations under the License.\
 *\
 */\
metadata \{\
        definition (name: "Virtual Switch", namespace: "cben0ist", author: "cben0ist") \{\
        capability "Switch"\
        capability "Refresh"  \
		capability "Actuator"      \
    \}\
\
	// simulator metadata\
	simulator \{\
	\}\
\
	// UI tile definitions\
	tiles \{\
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: false) \{\
			state "off", label: 'Off', action: "switch.on", icon: "st.Kids.kid10", backgroundColor: "#ffffff", nextState: "on"\
			state "on", label: 'On', action: "switch.off", icon: "st.Kids.kid10", backgroundColor: "#79b821", nextState: "off"\
		\}\
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") \{\
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"\
		\}        \
		main "button"\
		details(["button", "refresh"])\
	\}\
\}\
\
def parse(String description) \{\
\}\
\
def on() \{\
	sendEvent(name: "switch", value: "on")\
\}\
\
def off() \{\
	sendEvent(name: "switch", value: "off")\
\}}