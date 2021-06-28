#!/bin/bash

#ip=$(multipass info mongo | grep IPv4 | awk '{print $2}')
#vm_domain="mongo.home.smart"
#multipass exec broker -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"

ip=$(multipass info gateway | grep IPv4 | awk '{print $2}')
vm_domain="gateway.home.smart"
multipass exec things -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"

#ip=$(multipass info mqtt | grep IPv4 | awk '{print $2}')
#vm_domain="mqtt.home.smart"
#multipass exec gateway -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"

ip=$(multipass info devices | grep IPv4 | awk '{print $2}')
vm_domain="devices.home.smart"
multipass exec things -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"
