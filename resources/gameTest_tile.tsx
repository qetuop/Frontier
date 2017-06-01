<?xml version="1.0" encoding="UTF-8"?>
<tileset name="gameTest_tile" tilewidth="32" tileheight="32" tilecount="4" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0" type="tree">
  <properties>
   <property name="blocked" type="bool" value="true"/>
  </properties>
  <image width="32" height="32" source="tree.png"/>
 </tile>
 <tile id="1" type="stone">
  <properties>
   <property name="blocked" type="bool" value="true"/>
  </properties>
  <image width="32" height="32" source="stone.png"/>
 </tile>
 <tile id="2" type="grass">
  <properties>
   <property name="blocked" type="bool" value="false"/>
  </properties>
  <image width="32" height="32" source="grass.png"/>
 </tile>
 <tile id="3" type="char">
  <properties>
   <property name="blocked" type="bool" value="true"/>
  </properties>
  <image width="32" height="32" source="char.png"/>
 </tile>
</tileset>
