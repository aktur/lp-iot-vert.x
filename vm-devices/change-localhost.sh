#!/bin/bash

ip=$(multipass info gateway | grep IPv4 | awk '{print $2}')
vm_domain="gateway.home.smart"
multipass exec devices -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"

#ip=$(multipass info mqtt | grep IPv4 | awk '{print $2}')
#vm_domain="mqtt.home.smart"
#multipass exec devices -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"

ip=$(multipass info things | grep IPv4 | awk '{print $2}')
vm_domain="things.home.smart"
multipass exec devices -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"
