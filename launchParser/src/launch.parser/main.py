'''
Created on May 24, 2010

@author: chengdong
'''

import xml.dom.minidom
dir(xml.dom.minidom)
doc = xml.dom.minidom.parse('/workspaces/eclipse_rcp_352/workspace/python/Marketplace.launch')
for node in doc.getElementsByTagName('launchConfiguration'):
    for subNode in node.getElementsByTagName('stringAttribute'):
        key=subNode.getAttribute('key')
        if key=='target_bundles':
            val=subNode.getAttribute('value')
            bundles=val.split(',')
            bundles.sort()
            for bundle in bundles:
                print bundle
