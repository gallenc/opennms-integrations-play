# see https://markandruth.co.uk/2020/10/10/running-systemd-inside-a-centos-8-docker-container
FROM centos:8
# Set up base packages that are expected
RUN dnf -y install openssh-server crontabs NetworkManager firewalld selinux-policy
RUN systemctl mask dev-mqueue.mount dev-hugepages.mount \
     systemd-remount-fs.service sys-kernel-config.mount \
     sys-kernel-debug.mount sys-fs-fuse-connections.mount \
     graphical.target systemd-logind.service \
     NetworkManager.service systemd-hostnamed.service
STOPSIGNAL SIGRTMIN+3
# SSHd setup
EXPOSE 22
COPY docker.pub /root/.ssh/authorized_keys
RUN chmod 600 /etc/ssh/ssh_host* /root/.ssh/authorized_keys
CMD ["/sbin/init"]
