##Taken from AWS documentation##

###Step 1: Prerequisites###

We recommend that you use the latest versions of Git and other prerequisite software.

    You must install Python. To download and install the latest version of Python. View the Python website 
    You must install and configure a Git client. View Git downloads page 
    You must have permissions to the CodeCommit repository.
    You must install and configure the AWS CLI with a profile.
 
###Step 2: Install git-remote-codecommit###
At a terminal or command line, run the following command to install git-remote-codecommit:
pip install git-remote-codecommit

###Step 3: Clone the repository###

Clone your repository to your local computer and start working on code. Run the following command:
git config --global init.defaultBranch main
git clone codecommit::us-east-1://<name_of_local_aws_profile_mapped_to_aws_account>@nbme-dna-platform-preapp
