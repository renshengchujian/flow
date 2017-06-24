<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>FLow</title>
    <link rel="stylesheet" href="static/css/element.css"/>
    <link rel="stylesheet" href="static/css/fonts.css"/>
    <link rel="stylesheet" href="static/css/index.css"/>
</head>
<body>
<div id="app">

    <el-button type="primary" v-loading.fullscreen.lock="fullscreenLoading"></el-button>
    <el-row class="toolbar">
        <el-col :span="4">
            <el-tooltip :disabled="!form.toolTip" content="select" placement="top">
                <el-button id="pointer" :type="mode?'info':'default'" class="select"><span class="icon-select"></span>
                </el-button>
            </el-tooltip>
            <el-tooltip :disabled="!form.toolTip" content="path" placement="top">
                <el-button id="path" :type="mode?'default':'info'" class="select"><span class="icon-path"></span>
                </el-button>
            </el-tooltip>
        </el-col>

        <el-col :span="16">
            <el-tooltip :disabled="!form.toolTip" :content="type" placement="top"
                        v-for="(node,type) in nodeMap">
                <div class="el-button state el-tooltip el-button--default ui-draggable"
                     :type="type">
                    <span>
                        <span :class="addIcon(type)"></span>
                    </span>
                </div>
            </el-tooltip>
        </el-col>

        <el-col :span="4">
            <el-tooltip :disabled="!form.toolTip" content="Save" placement="top">
                <el-button type="default" id="myflow_save"><span class="icon-save"></span>
                </el-button>
            </el-tooltip>
            <el-upload action="upload" :before-upload="beforeUpload" :on-success="uploadSuccess" :on-error="uploadError"
                       class="text-left">
                <el-tooltip :disabled="!form.toolTip" content="Import Flow" placement="top">
                    <el-button type="default"><span class="icon-import"></span></el-button>
                </el-tooltip>
            </el-upload>
            <el-popover ref="settings" placement="bottom" trigger="click">
                <el-form ref="form" :model="form" label-width="80px">
                    <el-form-item label="Tooltip">
                        <el-switch on-text="" off-text="" v-model="form.toolTip"></el-switch>
                    </el-form-item>
                </el-form>
            </el-popover>

            <el-button v-popover:settings icon="setting"></el-button>
        </el-col>
    </el-row>

    <el-dialog title="config" :visible.sync="dialogVisible" :before-close="handleClose"
               :close-on-click-modal="false" :close-on-press-escape="false">
        <el-tabs type="border-card" @tab-click="handleClick" v-model="activeName">
            <el-tab-pane label="property" name="property" v-if="prop.show">
                <el-table :data="prop.propData" border @cell-dblclick="editProperty">
                    <el-table-column property="key" label="key"></el-table-column>
                    <el-table-column property="value" label="value">
                        <template scope="scope">
                            <el-input size="small" :value="scope.row.value.value" icon="check"
                                      class="editEl" @click="handleIconClick()"
                                      v-show="scope.row.value.editFlag"></el-input>
                            <span
                                v-show="!scope.row.value.editFlag">{{scope.row.value.value}}</span>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="rule" name="rule" v-if="rule.show">
                <el-button-group class="text-right">
                    <el-button type="primary" icon="plus" @click="addRule"></el-button>
                    <el-button type="primary" icon="delete" @click="deleteRule"
                               :disabled="rule.ruleData ===0 || !this.currentRow"></el-button>
                </el-button-group>
                <el-table :data="rule.ruleData" highlight-current-row border
                          @current-change="handleCurrentChange" @cell-dblclick="editProperty">
                    <el-table-column property="index" label="index" width="100">
                    </el-table-column>
                    <el-table-column property="type" label="type"></el-table-column>
                    <el-table-column property="fromNodeId" label="fromNodeId">
                        <template scope="scope">
                            <el-input size="small" :value="scope.row.fromNodeId.value"
                                      icon="check" class="editEl" @click="handleIconClick()"
                                      v-show="scope.row.fromNodeId.editFlag"></el-input>
                            <span v-show="!scope.row.fromNodeId.editFlag">{{scope.row.fromNodeId.value}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column property="rule" label="rule">
                        <template scope="scope">
                            <el-input size="small" :value="scope.row.rule.value" icon="check"
                                      class="editEl" @click="handleIconClick()"
                                      v-show="scope.row.rule.editFlag"></el-input>
                            <span
                                v-show="!scope.row.rule.editFlag">{{scope.row.rule.value}}</span>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="language" name="language" v-if="lan.show">
                <el-table :data="lan.lanData" border @cell-dblclick="editProperty">
                    <el-table-column property="type" label="type" width="80"></el-table-column>
                    <el-table-column property="language" label="language">
                        <template scope="scope">
                            <el-input size="small" :value="scope.row.language.value"
                                      icon="check" class="editEl" @click="handleIconClick()"
                                      v-show="scope.row.language.editFlag"></el-input>
                            <span v-show="!scope.row.language.editFlag">{{scope.row.language.value}}</span>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>
        </el-tabs>
    </el-dialog>

    <div id="myflow"></div>
</div>

<script type="text/javascript" src="static/js/vue.min.js"></script>
<script type="text/javascript" src="static/js/element.js"></script>
<script type="text/javascript" src="static/js/en.js"></script>
<script type="text/javascript" src="static/js/raphael-min.js"></script>
<script type="text/javascript" src="static/js/jquery.min.js-1.12.4.js"></script>
<script type="text/javascript" src="static/js/jquery-ui.min-1.12.1.js"></script>
<script type="text/javascript" src="static/js/myflow.js"></script>
<script type="text/javascript" src="static/js/index.js"></script>

</body>
</html>