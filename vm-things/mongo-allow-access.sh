#!/bin/bash

multipass --verbose exec things -- sudo -- bash << EOF
sed -i "s/bind_ip = 127.0.0.1/bind_ip = 0.0.0.0/" /etc/mongodb.conf

/etc/init.d/mongodb restart
EOF


#multipass --verbose exec mongo -- sudo -- bash << EOF
#/etc/init.d/mongodb restart
#EOF