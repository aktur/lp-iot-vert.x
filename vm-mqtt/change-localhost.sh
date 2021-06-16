#!/bin/bash

ip=$(multipass info gateway | grep IPv4 | awk '{print $2}')
vm_domain="gateway.home.smart"
multipass exec mqtt -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"

ip=$(multipass info devices | grep IPv4 | awk '{print $2}')
vm_domain="devices.home.smart"
multipass exec mqtt -- sudo -- sh -c "echo \"${ip} ${vm_domain}\" >> /etc/hosts"
