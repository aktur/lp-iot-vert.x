import {Style} from '../js/helper.js'

const template = document.createElement('template')
template.innerHTML = `
      <div class="column"> <!-- <div class="column is-one-quarter"> -->
        <div class="card">
          <header class="card-header">
            <p class="card-header-title title is-4" style="height:70px">
              <!-- title -->
            </p>
          </header>
          <div class="card-content">
            <div class="content">
              <!-- content -->
              <div class="device-info"></div>
              <hr>
              <!-- charts -->
              <canvas class="temperature-info"></canvas>
              <canvas class="humidity-info"></canvas>
              <canvas class="eco2-info"></canvas>

            </div> <!-- content -->
          </div> <!-- card-content -->
        </div> <!-- card -->
    </div> <!-- column -->
`

class DeviceCard extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: 'open'})
    this.shadowRoot.appendChild(template.content.cloneNode(true))
    this.shadowRoot.adoptedStyleSheets = [Style.sheets.bulma] // you can add several sheets
  }

  connectedCallback() {
    this.cardTitle = this.shadowRoot.querySelector(".card-header-title")
    this.deviceInfo = this.shadowRoot.querySelector(".device-info")
    this.temperatureInfo = this.shadowRoot.querySelector(".temperature-info")
    this.humidityInfo = this.shadowRoot.querySelector(".humidity-info")
    this.eCo2Info = this.shadowRoot.querySelector(".eco2-info")

    // Initialize and add graphs here

  }

  setTitle(title) {
  }

  setDeviceInfo(title) {
  }

  setTemperatureInfo(metrics) {
  }

  setHumidityInfo(metrics) {
  }

  setECo2Info(metrics) {
  }

}

customElements.define('device-card', DeviceCard)
