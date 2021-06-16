#!/bin/bash

#ip=$(multipass info redis | grep IPv4 | awk '{print $2}')
#vm_domain="redis.home.smart"
#multipass exec gateway -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"

#ip=$(multipass info mqtt | grep IPv4 | awk '{print $2}')
#vm_domain="mqtt.home.smart"
#multipass exec gateway -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"

ip=$(multipass info devices | grep IPv4 | awk '{print $2}')
vm_domain="devices.home.smart"
multipass exec gateway -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"

#ip=$(multipass info broker | grep IPv4 | awk '{print $2}')
#vm_domain="broker.home.smart"
#multipass exec gateway -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"

ip=$(multipass info things | grep IPv4 | awk '{print $2}')
vm_domain="things.home.smart"
multipass exec gateway -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"