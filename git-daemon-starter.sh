#!/bin/bash
if [ -z ${REPO_HOME} ]; then
  echo 'REPO_HOME required'
  exit 0
fi

sudo tee /etc/systemd/system/git-daemon.service <<EOF
[Unit]
Description=git-daemon
After=syslog.target network.target

[Service]
Type=exec
ExecStart=git daemon --export-all --reuseaddr --enable=receive-pack --verbose --base-path=${REPO_HOME}
WorkingDirectory=${REPO_HOME}
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
EOF

# sudo systemctl daemon-reload
# sudo systemctl start git-daemon.service
# sudo systemctl enable git-daemon.service
# sudo systemctl status git-daemon.service
# sudo journalctl -f -u git-daemon