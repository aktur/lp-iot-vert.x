import {Style} from '../js/helper.js'

import './top-bar.js'
import './devices-list.js'


const template = document.createElement('template')
template.innerHTML = `
<div>
    <top-bar></top-bar>
    <div> <!-- class="container" -->
        <section id="devices" class="section" style="display:block">
            <devices-list></devices-list>
        </section>
    </div>
</div>
`

class MainApplication extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: 'open'})
    this.shadowRoot.appendChild(template.content.cloneNode(true))
    this.shadowRoot.adoptedStyleSheets = [Style.sheets.bulma] // you can add several sheets

    this.devices = {}
    this.eventBus = null

    // the main application is listening
    this.addEventListener('is-authenticated', this.onAuthenticated)
    this.addEventListener('is-disconnected', this.onDisconnected)
  }

  connectedCallback() {
    this.devicesList = this.shadowRoot.querySelector("devices-list")
  }

  onDisconnected(message) {
    if(message.type==='is-disconnected') {
      this.eventBus.close();
    }
  }

  onAuthenticated(message) {
    if(message.type==='is-authenticated') {

      this.eventBus = new EventBus(`/eventbus`)

      this.eventBus.onopen = _ => {

        this.eventBus.registerHandler("service.message",(error, message) => {
          let data = JSON.parse(message.body)

          let device = data["device"]

          if(!this.devices[device.id]) { // this is a new device => notify the devicesList
            this.devices[device.id]={
              topic: data.topic,
              category: device.category,
              location: device.location,
              date: data.date,
              hour: data.hour,
              parsedData: 0,
              oldParsedData: 0,
              sensors: {
                temperatureMetrics:[],
                humidityMetrics: [],
                eCO2Metrics: []
              }
            }
            // notify the devicesList
            this.devicesList.newDevice(device.id, this.devices[device.id])
          }

          // update date and hour
          this.devices[device.id].date = data.date
          this.devices[device.id].hour = data.hour

          this.devices[device.id].parsedData = Date.parse(`${data.date} ${data.hour}`)

          // we cannot manage the order of the data
          if (this.devices[device.id].parsedData>this.devices[device.id].oldParsedData) {
            // update the data of every sensors
            let temperatureMetrics = this.devices[device.id].sensors.temperatureMetrics
            let humidityMetrics = this.devices[device.id].sensors.humidityMetrics
            let eCO2Metrics = this.devices[device.id].sensors.eCO2Metrics

            temperatureMetrics.push(device.sensors.find(sensor=>sensor["temperature"]!==undefined)["temperature"].value)
            humidityMetrics.push(device.sensors.find(sensor=>sensor["humidity"]!==undefined)["humidity"].value)
            eCO2Metrics.push(device.sensors.find(sensor=>sensor["eCO2"]!==undefined)["eCO2"].value)

            if(temperatureMetrics.length>10) {temperatureMetrics.shift()}
            if(humidityMetrics.length>10) {humidityMetrics.shift()}
            if(eCO2Metrics.length>10) {eCO2Metrics.shift()}

            // notify the devicesList
            this.devicesList.newMetrics(device.id, this.devices[device.id])
          }

          this.devices[device.id].oldParsedData = this.devices[device.id].parsedData

        })
      }

    }
  }
}

customElements.define('main-application', MainApplication)
