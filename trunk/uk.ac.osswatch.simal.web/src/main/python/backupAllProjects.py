# Copyright 2010 University of Oxford
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
'''
This script writes a backup of all RDF/XML of the projects of a running
Simal instance. Written in/for Python 3.
'''
import json
import http.client
import os
import codecs

SIMAL_INSTANCE = 'localhost:8080'
SIMAL_REST_URL_PREFIX = '/simal-rest/project/source-simal/project-'
SIMAL_REST_URL_SUFFIX = '/xml'
PROJECT_FOLDER = 'allProjects/'

MAX_FILENAME_LENGTH = 20

def generate_path(projectLabel):
    '''Generate path to a file for specified label and guarantee file does not yet exist.'''
    suffix = '.xml'
    i = 1
    filePath = PROJECT_FOLDER + sanitize(projectLabel, MAX_FILENAME_LENGTH)

    while os.path.exists(filePath + suffix):
        if i < 100:
            filePath = filePath.rstrip('0' + str(i-1))
            if i < 10:
                filePath += '0'
            filePath += str(i)
        else:
            raise IOError('Could not find non-existent path for: ' + filePath)
        i += 1

    filePath += suffix
    return filePath

def sanitize(s, strLength):
    '''Remove special characters and cut off at specified length.'''
    s = s.replace(' ', '')
    s = s.replace('/', '')
    s = s.replace('\\', '')
    s = s.replace('$', '')
    s = s.replace('#', '')
    s = s.replace(':', '')
    s = s.replace('?', '')
    s = s.replace('!', '')
    s = s.replace('*', '')
    s = s.replace('<', '')
    s = s.replace('>', '')

    return s[:strLength]

def get_simal_connection():
    ''' Return a connection to the configured running Simal instance '''
    return http.client.HTTPConnection(SIMAL_INSTANCE)

def process_simal_project(simalID, projectLabel):
    '''Retrieve RDF/XML for the specified project and write it to file.'''
    simalProjectUrl = SIMAL_REST_URL_PREFIX + simalID + SIMAL_REST_URL_SUFFIX
    conn = get_simal_connection()
    try:
        filePath = generate_path(projectLabel)
        conn.request('GET', simalProjectUrl)
        response = conn.getresponse()

        if (response.status == http.client.OK):
            data1 = response.read().decode('utf-8', 'ignore')
            projectFile = open(filePath,'w',encoding='utf-8')
            projectFile.write(data1)
            projectFile.close()
        else:
            print('Problem getting project ' + simalID + '; reason: ' + response.reason)
    except Exception as e:
        msg = str(e)
        print('Could not process project \'' + projectLabel + '\': ' + msg)
        if msg.find('encode characters in position ') != -1:
            lstripped = msg[int(msg.find('position ') + 9):]
            probChar = int(lstripped[:lstripped.find('-')])
            print(simalProjectUrl + ' ' + data1[probChar-100:probChar+10])
        
def get_all_projects_json():
    '''Retrieve all projects as JSON from the running Simal instance.'''
    allProjectsJson = ''
    try:
        simalAllProjectsUrl = '/simal-rest/allProjects/json'
        conn = get_simal_connection()
        conn.request('GET', simalAllProjectsUrl)
        response = conn.getresponse()

        if (response.status == http.client.OK):
            allProjectsJson = response.read().decode('utf-8', 'ignore')
        else:
            msg = 'HTTP response was: ' + response.reason
            raise Exception(msg)
    except Exception as e:
        print('Problem getting all projects from Simal: ' + str(e))
        raise e

    return allProjectsJson

def init():
    ''' Make sure folder to write projects to exists.'''
    if not os.path.exists(PROJECT_FOLDER):
        os.makedirs(PROJECT_FOLDER)

def main():
    try:
        init()
        allProjectsJson = get_all_projects_json()

        # Next is only for Simal instances that don't yet escape JSON properly
        # See Issue 339 / 73
        allProjectsJson = allProjectsJson.replace('\\\'','')

        allProjects = json.loads(allProjectsJson)
        
        print('Backing up ' + str(len(allProjects.get('items'))) + ' projects.')

        for project in allProjects.get('items'):
            simalID = project.get('simalID')
            label = project.get('label')
            if not label.startswith('http://'): # Filter out empty projects
                process_simal_project(simalID, label)

    except Exception as e:
        msg = str(e)
        print('Problem backing up all projects: ' + msg)
        if msg.find('Invalid \\escape:') != -1:
            probChar = int(msg[int(msg.find('(char ') + 6):].rstrip(')'))
            print(allProjectsJson[probChar-100:probChar+10])
        raise e

if __name__ == "__main__":
    main()
