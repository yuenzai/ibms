#!/bin/bash
if [ -z ${REPO_HOME} ]; then
  echo 'REPO_HOME required'
  exit 0
fi

if [ -z ${REPO_WORK_DIR_HOME} ]; then
  echo 'REPO_WORK_DIR_HOME required'
  exit 0
fi

if [ -z ${1} ]; then
  echo 'pass repo name'
  exit 0
fi

REPO_NAME=${1}
REPO_PATH=${REPO_HOME}/${REPO_NAME}.git
REPO_WORK_DIR=${REPO_WORK_DIR_HOME}/${REPO_NAME}

git init --bare ${REPO_PATH}
mkdir -p ${REPO_WORK_DIR}

tee ${REPO_PATH}/hooks/post-receive <<EOF
#!/bin/bash
LOG_FILE=${REPO_PATH}/hooks/post-receive.log

while read oldrev newrev refname; do
    echo \$refname >> \$LOG_FILE
    if [[ "\$refname" == "refs/heads/master" ]]; then
        git --work-tree="${REPO_WORK_DIR}" --git-dir="${REPO_PATH}" checkout -f "master"
    fi
done
EOF

chmod +x ${REPO_PATH}/hooks/post-receive
