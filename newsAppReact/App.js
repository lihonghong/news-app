import React, {Component} from 'react';
import {
    ActivityIndicator,
    StyleSheet,
    Text,
    View,
    ListView,
    Linking
} from 'react-native';

export default class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: true
        }
    }

    componentDidMount() {
        return fetch('http://hotquery.lbsso.com/') // 获取热词列表
            .then((response) => response.json())
            .then((responseJson) => {
                let ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
                this.setState({
                    isLoading: false,
                    dataSource: ds.cloneWithRows(responseJson.data),
                }, function () {
                    // do something with new state
                });
            })
            .catch((error) => {
                console.error(error);
            });
    }

    render() {
        if (this.state.isLoading) {
            return (
                <View style={{flex: 1, paddingTop: 20}}>
                    <ActivityIndicator/>
                </View>
            );
        }

        return (
            <View style={styles.container}>
                <Text/>
                <ListView
                    dataSource={this.state.dataSource}
                    renderRow={(rowData) =>
                        <Text style={styles.title} onPress={
                            () => {
                                Linking.openURL('https://www.baidu.com/s?wd=' + rowData.name) // 跳转到百度搜索此热词
                            }}>{rowData.name}
                        </Text>}
                />
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5FCFF',
    },
    title:{
        color:"black",
        fontSize:20,
        padding: 10,
    }
});
